package local.messaging.listener;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableConfigurationProperties(ListenerApplication.ServiceBusProps.class)
public class ListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListenerApplication.class, args);
    }

    @Bean(destroyMethod = "close")
    ServiceBusProcessorClient processor(ServiceBusProps props) {
        return new ServiceBusClientBuilder()
                .connectionString(props.connectionString())
                .processor()
                .queueName(props.queueName())
                .maxConcurrentCalls(props.maxConcurrentCalls())
                .processMessage(this::onMessage)
                .processError(this::onError)
                // be explicit about settlement
                .disableAutoComplete()
                .buildProcessorClient();
    }

    @Bean
    ApplicationRunner runner(ServiceBusProcessorClient processor, ServiceBusProps props) {
        return args -> {
            System.out.printf("Listening on queue '%s'...%n", props.queueName());
            processor.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Stopping listener...");
                processor.close();
            }));

            // Keep the JVM alive.
            new CountDownLatch(1).await();
        };
    }

    private void onMessage(ServiceBusReceivedMessageContext context) {
        String body = context.getMessage().getBody().toString();
        System.out.printf("Received message id=%s subject=%s body=%s%n",
                context.getMessage().getMessageId(),
                context.getMessage().getSubject(),
                body);

        context.complete();
    }

    private void onError(ServiceBusErrorContext context) {
        System.err.printf("ServiceBus error source=%s entity=%s exception=%s%n",
                context.getErrorSource(),
                context.getEntityPath(),
                context.getException());
    }

    @ConfigurationProperties(prefix = "app.servicebus")
    public record ServiceBusProps(String connectionString, String queueName, int maxConcurrentCalls) {
    }
}
