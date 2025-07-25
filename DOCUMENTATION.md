# Project Documentation: Quidax Chart Data (v2)

## Project Overview
The Quidax Chart Data project is a Spring Boot microservice that provides recent historical trade data for a specific cryptocurrency market. This service is designed to be consumed by a frontend application to render visualizations like price charts.

It correctly handles the Quidax API's response structure by using specific Data Transfer Objects (DTOs) to parse the nested JSON and deliver a clean list of trade data.

### API Endpoint
`GET /api/v1/charts/{market}/trades`: Fetches a list of recent trades for a given market pair (e.g., btcngn).

## Core Dependencies
- **spring-boot-starter-web**: Provides all necessary components for building REST APIs, including an embedded Tomcat server and the Jackson JSON library.
- **lombok**: A utility library used to reduce boilerplate code like getters and setters.

## Project Structure and Components
The project follows a standard layered architecture. The DTO package models the nested structure of the Quidax API response for the trades endpoint.

```
/dto/
 ├── Trade.java           (Innermost: A single trade record)
 └── TradesResponse.java  (Wrapper for the API's top-level response)
/service/
 └── ChartService.java    (Business Logic and API communication)
/controller/
 └── ChartController.java (API Endpoint Layer)
```

## Detailed Class Explanations

### The DTO Layer (The Data Models)
The DTOs are designed to exactly match the JSON structure from the Quidax API.

#### 📄 Trade.java
**Purpose**: Represents the innermost object containing the details of a single historical trade.

**Code**:
```java
package com.quidaxproject.chart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Trade {

    private String type;
    private String price;
    private long timestamp;

    @JsonProperty("trade_id")
    private int tradeId;

    @JsonProperty("base_volume")
    private String baseVolume;

    @JsonProperty("quote_volume")
    private String quoteVolume;
}
```

#### 📄 TradesResponse.java
**Purpose**: Represents the top-level wrapper object for the /trades/{market} endpoint response. It contains the status and the list of trades.

**Code**:
```java
package com.quidaxproject.chart.dto;

import lombok.Data;
import java.util.List;

@Data
public class TradesResponse {
    private String status;
    private List<Trade> data;
}
```

### service/ChartService.java - The Business Logic
This class is responsible for calling the Quidax API and "unwrapping" the nested JSON to extract the list of trades.

#### getRecentTrades(String market) Method:
- **Action**: Calls the `https://api.quidax.com/api/v1/trades/{market}` endpoint.
- **Logic**: It tells RestTemplate to expect a TradesResponse object. It then drills down (`response.getData()`) to extract and return the final `List<Trade>`. This unwrapping logic is key to handling the API's structure.

**Code:**

```java
package com.quidaxproject.chart.service;

import com.quidaxproject.chart.dto.Trade;
import com.quidaxproject.chart.dto.TradesResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@Service
public class ChartService {

    private final RestTemplate restTemplate;

    public ChartService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Trade> getRecentTrades(String market) {
        String url = "https://api.quidax.com/api/v1/trades/" + market;

        TradesResponse response = restTemplate.getForObject(url, TradesResponse.class);

        if (response != null && "success".equals(response.getStatus())) {
            return response.getData();
        }

        return Collections.emptyList();
    }
}
```

## controller/ChartController.java - The API Layer

This class remains a thin layer that handles incoming HTTP requests, extracts the market from the URL, and delegates the work to the `ChartService`.

### getTradesForMarket(@PathVariable String market) Method:

- **Action:** Handles GET requests to `/api/v1/charts/{market}/trades`.
- **Logic:** It uses the `@PathVariable` annotation to capture the market pair from the URL and passes it to the `ChartService`. It returns the list of trades provided by the service, which Spring Boot automatically converts to a JSON array.

**Code:**

```java
package com.quidaxproject.chart.controller;

import com.quidaxproject.chart.dto.Trade;
import com.quidaxproject.chart.service.ChartService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/charts")
public class ChartController {

    private final ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/{market}/trades")
    public List<Trade> getTradesForMarket(@PathVariable String market) {
        return chartService.getRecentTrades(market);
    }
}
```
