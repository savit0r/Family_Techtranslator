package com.familytech.translator.dto;

import java.time.ZonedDateTime;

public record HealthResponse(
    String status,
    String dbStatus,
    String service,
    ZonedDateTime timestamp
) {
    public static HealthResponse up(String dbStatus) {
        return new HealthResponse("UP", dbStatus, "family-tech-translator", ZonedDateTime.now());
    }
}
