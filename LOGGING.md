# Logging Setup Guide

This project is configured with SLF4J and Logback for comprehensive logging, including support for distributed logging systems.

## Overview

The logging setup includes:
- **SLF4J** - Logging facade (already included with Spring Boot)
- **Logback** - Logging implementation
- **Logstash Encoder** - JSON formatting for distributed logging (ELK, Splunk, etc.)
- **Correlation ID Filter** - Request tracing with unique IDs
- **MDC Support** - Contextual logging (traceId, userId, sessionId)

## Log Files

Three types of logs are generated in the `logs/` directory:

1. **app.log** - Human-readable format for development
2. **app-json.log** - JSON format for distributed logging systems
3. **Console output** - Human-readable format

Log files rotate daily and keep 30 days of history.

## Basic Usage

### 1. Simple Logging in Your Class

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyService {
    private static final Logger log = LoggerFactory.getLogger(MyService.class);

    public void myMethod() {
        log.debug("Debug message for development");
        log.info("Information message");
        log.warn("Warning message");
        log.error("Error message");

        // Parameterized logging (recommended for performance)
        String username = "john";
        log.info("User logged in: {}", username);

        // Multiple parameters
        log.info("Order {} created for customer {} with amount {}",
                 orderId, customerId, amount);

        // Exception logging
        try {
            // ... code
        } catch (Exception e) {
            log.error("Failed to process order: {}", orderId, e);
        }
    }
}
```

### 2. Structured Logging with Context

Use the `LoggingHelper` utility for adding business context:

```java
import com.rekreation.learning.vaadin.util.LoggingHelper;
import java.util.Map;

public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public void createOrder(Order order) {
        LoggingHelper.logWithContext(log, "Order created successfully", Map.of(
            "orderId", order.getId(),
            "customerId", order.getCustomerId(),
            "amount", order.getAmount(),
            "status", order.getStatus()
        ));
    }

    public void handleError(Exception e, String orderId) {
        LoggingHelper.logErrorWithContext(log, "Failed to process order", e, Map.of(
            "orderId", orderId,
            "errorType", e.getClass().getSimpleName()
        ));
    }
}
```

### 3. Correlation IDs (Request Tracing)

The `CorrelationIdFilter` automatically adds a unique `traceId` to every HTTP request:

- **Automatic**: No code changes needed
- **Console logs**: Shows as `[traceId]` in the log pattern
- **JSON logs**: Included as a field for querying
- **Response header**: `X-Trace-Id` returned to client

All logs for the same request will have the same traceId, making it easy to trace request flow.

## Log Levels

Configure log levels in `application.properties`:

```properties
# Your application - DEBUG level
logging.level.com.rekreation=DEBUG

# All other packages - INFO level
logging.level.root=INFO

# Specific package - TRACE level
logging.level.com.rekreation.learning.vaadin.service=TRACE
```

## JSON Log Format

The `app-json.log` file contains structured JSON logs:

```json
{
  "@timestamp": "2025-12-21T10:30:45.123Z",
  "level": "INFO",
  "logger_name": "com.rekreation.learning.vaadin.service.OrderService",
  "message": "Order created successfully",
  "thread_name": "http-nio-8080-exec-1",
  "app_name": "vaadin-example",
  "environment": "dev",
  "traceId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "userId": "tushar",
  "sessionId": "F1A2B3C4D5E6F7G8H9I0",
  "orderId": "12345",
  "customerId": "CUST-001",
  "amount": "99.99"
}
```

This format is ideal for:
- **ELK Stack** (Elasticsearch, Logstash, Kibana)
- **Splunk**
- **Grafana Loki**
- **CloudWatch Logs**
- **Datadog**

## Integration with Distributed Logging

### ELK Stack Example

1. **Filebeat** configuration to ship logs:
```yaml
filebeat.inputs:
  - type: log
    enabled: true
    paths:
      - /path/to/logs/app-json.log
    json.keys_under_root: true
    json.add_error_key: true

output.elasticsearch:
  hosts: ["localhost:9200"]
```

2. Query by traceId in Kibana:
```
traceId: "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
```

### Splunk Example

Add monitor in Splunk:
```
sourcetype = _json
source = /path/to/logs/app-json.log
```

Query: `index=main traceId="a1b2c3d4-e5f6-7890-abcd-ef1234567890"`

## Best Practices

1. **Use parameterized logging** for performance:
   ```java
   // Good - lazy evaluation
   log.debug("Processing order: {}", orderId);

   // Bad - always creates string
   log.debug("Processing order: " + orderId);
   ```

2. **Choose appropriate log levels**:
   - `TRACE` - Very detailed, method entry/exit
   - `DEBUG` - Debugging information
   - `INFO` - Important business events
   - `WARN` - Recoverable issues
   - `ERROR` - Errors requiring attention

3. **Don't log sensitive data**:
   ```java
   // Bad
   log.info("User password: {}", password);

   // Good
   log.info("User authenticated: {}", username);
   ```

4. **Add context to errors**:
   ```java
   try {
       processPayment(orderId);
   } catch (Exception e) {
       log.error("Payment processing failed for order: {}", orderId, e);
   }
   ```

5. **Use MDC for request-scoped data**:
   ```java
   MDC.put("customerId", customerId);
   // All subsequent logs in this thread will include customerId
   MDC.remove("customerId"); // Clean up when done
   ```

## Configuration Files

- **pom.xml** - Logstash encoder dependency
- **logback-spring.xml** - Logback configuration
- **application.properties** - Log levels and basic settings
- **CorrelationIdFilter.java** - Adds traceId to requests
- **LoggingHelper.java** - Utility for structured logging

## Customization

### Disable JSON Logging

Remove from `logback-spring.xml`:
```xml
<appender-ref ref="JSON_FILE" />
```

### Change Log File Location

In `application.properties`:
```properties
logging.file.path=/custom/path/logs
```

### Add Custom Fields to All Logs

Edit `logback-spring.xml`:
```xml
<customFields>{"app_name":"vaadin-example","version":"1.0.0","region":"us-east"}</customFields>
```

## Monitoring Production

1. **Track error rates** by querying `level: ERROR`
2. **Monitor slow operations** by logging execution times
3. **Trace user journeys** using traceId
4. **Alert on exceptions** by pattern matching
5. **Analyze business metrics** from structured logs

## Example: Adding Logging to a New Service

```java
package com.rekreation.learning.vaadin.service;

import com.rekreation.learning.vaadin.util.LoggingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    public Customer createCustomer(CustomerDTO dto) {
        log.debug("Creating customer with email: {}", dto.getEmail());

        try {
            Customer customer = new Customer(dto);
            customerRepository.save(customer);

            LoggingHelper.logWithContext(log, "Customer created successfully", Map.of(
                "customerId", customer.getId(),
                "email", customer.getEmail(),
                "source", dto.getSource()
            ));

            return customer;
        } catch (Exception e) {
            LoggingHelper.logErrorWithContext(log, "Failed to create customer", e, Map.of(
                "email", dto.getEmail(),
                "errorType", e.getClass().getSimpleName()
            ));
            throw e;
        }
    }
}
```

## Testing Logs

Run your application and check:
1. Console output shows human-readable logs with `[traceId]`
2. `logs/app.log` contains human-readable logs
3. `logs/app-json.log` contains JSON-formatted logs
4. Make a request and note the `X-Trace-Id` header in the response
5. Find all logs for that request using the traceId

## Questions?

- SLF4J docs: https://www.slf4j.org/
- Logback docs: https://logback.qos.ch/
- Logstash encoder: https://github.com/logfellow/logstash-logback-encoder