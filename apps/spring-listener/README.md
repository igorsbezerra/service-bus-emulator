# spring-listener

Spring Boot app that consumes messages from the local Azure Service Bus Emulator.

## Run

Start the emulator (from repo root):

```zsh
docker compose up -d
```

Run the listener:

```zsh
cd apps/spring-listener
mvn -q spring-boot:run
```

Then, in another terminal, run the sender:

```zsh
cd apps/spring-sender
mvn -q spring-boot:run -Dspring-boot.run.arguments="--message=teste"
```

Env vars (optional):

- `SERVICEBUS_CONNECTION_STRING` (defaults to emulator connection string)
- `SERVICEBUS_QUEUE_NAME` (defaults to `queue.1`)
- `SERVICEBUS_MAX_CONCURRENT_CALLS` (defaults to `1`)
