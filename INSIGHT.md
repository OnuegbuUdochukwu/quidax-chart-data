# Core Purpose & Scope

The primary purpose of this application is to act as a specialized proxy and data provider for the Quidax API. It isolates the complexity of interacting with the external API and exposes a clean, simple endpoint for a client (like a frontend charting library) to consume.

## Business Logic:

- **External API Communication:** The core logic involves making HTTP GET requests to the Quidax trades endpoint using Spring's RestTemplate.

- **JSON Deserialization:** It handles the specific, nested JSON structure returned by Quidax. This "unwrapping" logic translates the complex external API response into a simple list of Trade objects.

- **Data Transformation:** It maps JSON fields with snake_case (e.g., trade_id) to standard Java camelCase fields (tradeId) using `@JsonProperty`.

## Key Services Exposed:

The application exposes a single RESTful endpoint:

`GET /api/v1/charts/{market}/trades`

This endpoint returns a JSON array of recent trades for the specified market pair.

# Application Architecture

The application uses a classic three-layer architecture, which is standard for Spring Boot web services.

## Entry Points:

Requests are ingested by the embedded Tomcat web server that comes with the spring-boot-starter-web dependency. Spring Boot automatically configures this server to listen for HTTP requests on a specified port (defaulting to 8080).

## Layered Design:

- **Controllers (Request Handling):** The `ChartController.java` class is the entry point for application code.

  It uses the `@RestController` annotation to handle web requests.

  The `@GetMapping("/{market}/trades")` annotation maps incoming GET requests to the appropriate method.

  It uses `@PathVariable` to extract the market string from the URL and passes it to the service layer. It does not contain any business logic.

- **Services (Business Logic):** The `ChartService.java` class contains the core application logic.

  It is responsible for constructing the correct URL for the external Quidax API.

  It uses `RestTemplate` to execute the HTTP call.

  Its most important function is to map the nested JSON response to our DTOs (`TradesResponse`) and "unwrap" the final, clean data (`List<Trade>`) that the controller will return.

- **Models/DTOs (Data Structures):** The DTOs in the `dto` package serve as data contracts.

    - `TradesResponse.java:` Models the top-level wrapper object from the Quidax API, allowing Jackson to parse the status and data fields.

    - `Trade.java:` Models the innermost object representing a single trade. It uses `@JsonProperty` to map API field names to our internal Java field names.

# Assumptions & Technical Debt

This application was built for a specific, simple purpose and carries some technical debt and limitations that would need to be addressed in a production environment.

## Known Limitations:

- **No API Failure Handling:** The code assumes the Quidax API will always be available and return a successful response. It doesn't handle server errors (5xx), timeouts, or other connection issues.
- **No Retry Logic:** A single failed API call results in an error for the end-user. Production systems often implement retry mechanisms (e.g., with exponential backoff) for transient network failures.
- **No Caching:** The application calls the Quidax API for every single request. For frequently requested markets, implementing a cache (like Caffeine or Redis) would significantly improve performance and reduce the load on the external API.

## Scalability & Security Risks:

- **Rate Limiting:** The application does not handle API rate limits from Quidax. If it receives a high volume of traffic, it could get blocked by the Quidax server, resulting in 429 Too Many Requests errors.
- **No Input Validation:** The `{market}` parameter is not validated. A user could pass an invalid market pair, causing the application to make a pointless API call that will always result in an error.
- **Statelessness (A Benefit):** The application is stateless (it stores no data between requests), which is a major advantage for scalability. It can be easily scaled horizontally by running multiple instances behind a load balancer.

## Tools