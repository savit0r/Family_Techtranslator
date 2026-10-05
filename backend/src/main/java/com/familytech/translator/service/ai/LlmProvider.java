package com.familytech.translator.service.ai;

import com.familytech.translator.dto.AiGenerateRequest;
import com.familytech.translator.dto.AiGenerateResponse;

public interface LlmProvider {
    AiGenerateResponse generate(AiGenerateRequest request);
    boolean isAvailable();
    String getProviderName();
}
