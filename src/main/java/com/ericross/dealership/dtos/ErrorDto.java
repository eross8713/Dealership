package com.ericross.dealership.dtos;

import java.time.OffsetDateTime;

// immutable record to represent error information in a structured way, including timestamp, status code, message, and trace ID for debugging purposes.
public record ErrorDto (OffsetDateTime timestamp,
                        int status,
                        String message,
                        String traceId)
{}
