package com.rekreation.learning.vaadin.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter that adds a correlation ID (traceId) to every HTTP request.
 * The traceId is stored in MDC and will be automatically included in all logs.
 * This enables distributed tracing across multiple services.
 */
@Component
@Order(1)
public class CorrelationIdFilter implements Filter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String TRACE_ID_MDC_KEY = "traceId";
    private static final String USER_ID_MDC_KEY = "userId";
    private static final String SESSION_ID_MDC_KEY = "sessionId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            // Get or generate trace ID
            String traceId = httpRequest.getHeader(TRACE_ID_HEADER);
            if (traceId == null || traceId.isEmpty()) {
                traceId = UUID.randomUUID().toString();
            }

            // Add trace ID to MDC (will be included in all logs)
            MDC.put(TRACE_ID_MDC_KEY, traceId);

            // Add trace ID to response header for client tracking
            httpResponse.setHeader(TRACE_ID_HEADER, traceId);

            // Optionally add session ID if available
            if (httpRequest.getSession(false) != null) {
                MDC.put(SESSION_ID_MDC_KEY, httpRequest.getSession().getId());
            }

            // Optionally add user ID if authenticated
            if (httpRequest.getUserPrincipal() != null) {
                MDC.put(USER_ID_MDC_KEY, httpRequest.getUserPrincipal().getName());
            }

            chain.doFilter(request, response);
        } finally {
            // Always clean up MDC to prevent memory leaks
            MDC.clear();
        }
    }
}