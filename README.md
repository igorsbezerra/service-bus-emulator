# Local Messaging Emulator

This project emulates a local messaging service using Docker containers for Azure SQL Edge and Azure Service Bus. It provides a simple setup for development and testing purposes.

## Project Structure

- **docker-compose.yml**: Defines the services for the local messaging emulator, including the Azure SQL Edge and Azure Service Bus containers. It specifies the images, container names, environment variables, ports, and volumes for each service.
  
- **.env**: Contains environment variables used in the `docker-compose.yml` file, such as database credentials and service configurations.

- **sql/init.sql**: An SQL script that initializes the Azure SQL Edge database with necessary tables, data, or configurations when the container starts.

- **service-bus/config/settings.example.json**: Provides an example configuration for the Azure Service Bus, detailing settings such as connection strings and queue configurations.

- **scripts/wait-for-services.sh**: A script used to wait for the Azure SQL Edge and Azure Service Bus services to be fully up and running before executing any dependent operations.

## Setup Instructions

1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Create/adjust the `.env` file with your configurations (at minimum: `SQL_SA_PASSWORD`, `SB_USERNAME`, `SB_PASSWORD`).
4. Run `docker compose up -d` to start the services.
5. The `sql-init` service applies `sql/init.sql` into the `local_messaging` database on first boot.

## Usage Guidelines

- Ensure Docker is installed and running on your machine.
- Modify the `.env` file to set your desired credentials and configurations.
- Access the Azure SQL Edge database on port `1433` and the Azure Service Bus on port `5672`.
- The Service Bus emulator connects to SQL via the compose network hostname `sqledge` (not `localhost`).

## Additional Information

For any issues or contributions, please refer to the project's issue tracker or contact the maintainers.