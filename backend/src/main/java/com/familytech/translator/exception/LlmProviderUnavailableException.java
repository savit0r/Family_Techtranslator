package com.familytech.translator.exception;

public class LlmProviderUnavailableException extends RuntimeException {
    public LlmProviderUnavailableException(String message) {
        super(message);
    }

    public LlmProviderUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
