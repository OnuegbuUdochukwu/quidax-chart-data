# Quidax Chart Data API 📊

A Spring Boot REST API that provides recent historical trade data for a specific cryptocurrency market from the public Quidax API. This service is designed to provide the necessary data for a frontend application to render a price chart.

## Features

- Fetches a list of recent historical trades for a given market pair (e.g., `btcngn`).
- Accurately models and parses the nested JSON response from the Quidax trades endpoint.
- Built with a clean, layered architecture (Controller, Service, DTO).

## Technologies Used

- Java 17
- Spring Boot 3
- Maven
- Lombok

## API Endpoint

| Method | Endpoint                     | Description                                             |
|--------|------------------------------|---------------------------------------------------------|
| GET    | `/api/v1/charts/{market}/trades` | Get recent historical trade data for a specific market. |

## Export to Sheets

### Example Usage:

A GET request to `http://localhost:8080/api/v1/charts/btcngn/trades` will return a JSON array of recent trades:

```json
[
    {
        "type": "sell",
        "price": "180321000.0",
        "timestamp": 1662552000,
        "tradeId": 12345,
        "baseVolume": "0.001",
        "quoteVolume": "180321.0"
    },
    {
        "type": "buy",
        "price": "180321001.0",
        "timestamp": 1662552005,
        "tradeId": 12346,
        "baseVolume": "0.002",
        "quoteVolume": "360642.0"
    }
]
```

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

- JDK (Java Development Kit) 17 or later
- Maven

### Installation & Running the App

Clone the repository:

```bash
git clone https://github.com/your-username/quidax-chart-data.git
```

Navigate to the project directory:

```bash
cd quidax-chart-data
```

Run the application using the Maven wrapper:

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`.