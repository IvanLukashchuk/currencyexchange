# Currency Exchange API

## Project Overview
This project is a REST API for managing currencies and fetching exchange rates. It retrieves exchange rates from an external service and stores them in a database. Exchange rates are updated every hour using a scheduler and cached in memory for faster access.

## Features:
- Retrieve a list of tracked currencies.
- Retrieve exchange rates for a specific currency.
- Add a new currency to track.
- Scheduled task to automatically update exchange rates every hour.

## Technologies Used:
- **Java 17**
- **Spring Boot**
- **Gradle**
- **PostgreSQL**
- **Liquibase** for database migrations
- **Lombok** for reducing boilerplate code
- **RestTemplate** for external API calls
- **JUnit 5** and **Mockito** for testing

## Installation and Running

### Prerequisites:
Ensure the following are installed on your system:
- JDK 17+
- Docker and Docker Compose
- Gradle

### Steps to Run the Application:
1. Clone the repository
  

2. Start PostgreSQL using Docker Compose:
    ```bash
    docker-compose up -d
    ```

3. Run the application:
    ```bash
    ./gradlew bootRun
    ```

4. Run tests:
    ```bash
    ./gradlew test
    ```

## API Endpoints:
- **GET** `/api/currency/list`  
  Retrieves a list of all tracked currencies.

- **GET** `/api/currency/rates/{currencyCode}`  
  Retrieves exchange rates for a specific currency.

- **POST** `/api/currency/add?currencyCode={currencyCode}`  
  Adds a new currency for tracking.

## Environment Variables:
- `EXCHANGE_RATE_API_KEY`: API key for the external exchange rate service. This should be configured in your environment.

## Example Request:
- **Add a new currency:**
    ```bash
    curl -X POST "http://localhost:8080/api/currency/add?currencyCode=USD"
    ```

- **Get exchange rates for a currency:**
    ```bash
    curl "http://localhost:8080/api/currency/rates/USD"
    ```

- **Get list of all tracked currencies:**
    ```bash
    curl "http://localhost:8080/api/currency/list"
    ```

## Contact
If you have any questions or feedback, feel free to reach out.