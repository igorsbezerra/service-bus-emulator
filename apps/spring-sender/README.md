# spring-sender

CLI Spring Boot app that sends 1 message to the local Azure Service Bus Emulator.

## Run

From the repo root, ensure the emulator is up:

```zsh
docker compose up -d
```

Send a message:

```zsh
cd apps/spring-sender
mvn -q spring-boot:run -Dspring-boot.run.arguments="--message=oi"
```

Env vars (optional):

- `SERVICEBUS_CONNECTION_STRING` (defaults to emulator connection string)
- `SERVICEBUS_QUEUE_NAME` (defaults to `queue.1`)
