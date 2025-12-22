package com.rekreation.learning.vaadin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

/**
 * Utility class for logging with additional context.
 * Demonstrates best practices for SLF4J logging.
 *
 * Usage examples:
 *
 * // Basic logging in your class:
 * private static final Logger log = LoggerFactory.getLogger(YourClass.class);
 * log.info("User logged in: {}", username);
 * log.error("Failed to save data", exception);
 *
 * // Structured logging with context:
 * LoggingHelper.logWithContext(log, "order.created", Map.of(
 *     "orderId", orderId,
 *     "amount", amount,
 *     "customerId", customerId
 * ));
 */
public class LoggingHelper {

    /**
     * Log a message with structured context data that will appear in JSON logs.
     * Useful for adding business context to log entries.
     *
     * @param logger The logger instance
     * @param message The log message
     * @param context Key-value pairs to add to MDC
     */
    public static void logWithContext(Logger logger, String message, Map<String, Object> context) {
        try {
            // Add context to MDC
            context.forEach((key, value) ->
                MDC.put(key, value != null ? value.toString() : "null")
            );

            // Log the message
            logger.info(message);
        } finally {
            // Clean up context
            context.keySet().forEach(MDC::remove);
        }
    }

    /**
     * Log an error with structured context data.
     */
    public static void logErrorWithContext(Logger logger, String message, Throwable throwable, Map<String, Object> context) {
        try {
            context.forEach((key, value) ->
                MDC.put(key, value != null ? value.toString() : "null")
            );
            logger.error(message, throwable);
        } finally {
            context.keySet().forEach(MDC::remove);
        }
    }

    /**
     * Add a custom field to MDC that will persist for the current request.
     * Useful for adding user context, tenant IDs, etc.
     */
    public static void addToMDC(String key, String value) {
        MDC.put(key, value);
    }

    /**
     * Remove a custom field from MDC.
     */
    public static void removeFromMDC(String key) {
        MDC.remove(key);
    }
}