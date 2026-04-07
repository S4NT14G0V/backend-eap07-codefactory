package com.codefactory.appstripe.common.api;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class ErrorResponse {
    String errorCode;
    String message;
    List<String> details;
    String traceId;
    Instant timestamp;
}
