package local.messaging.sender;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Instant;

@SpringBootApplication
@EnableConfigurationProperties(SenderApplication.ServiceBusProps.class)
public class SenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenderApplication.class, args);
    }

    @Bean(destroyMethod = "close")
    ServiceBusSenderClient serviceBusSenderClient(ServiceBusProps props) {
        return new ServiceBusClientBuilder()
                .connectionString(props.connectionString())
                .sender()
                .queueName(props.queueName())
                .buildClient();
    }

    @Bean
    ApplicationRunner runner(ServiceBusSenderClient sender, ServiceBusProps props) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) {
                String message = args.containsOption("message")
                        ? args.getOptionValues("message").get(0)
                        : "hello-from-sender";

                String payload = "{\"type\":\"demo-event\",\"message\":\"" + escapeJson(message) + "\",\"sentAt\":\"" + Instant.now() + "\"}";

                ServiceBusMessage sbMessage = new ServiceBusMessage(payload)
                        .setContentType("application/json")
                        .setSubject("demo-event");

                sender.sendMessage(sbMessage);
                System.out.printf("Sent 1 message to queue '%s': %s%n", props.queueName(), payload);

                // Exit after sending to keep this app as a simple CLI sender.
                System.exit(0);
            }
        };
    }

    private static String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @ConfigurationProperties(prefix = "app.servicebus")
    public record ServiceBusProps(String connectionString, String queueName) {
    }
}
