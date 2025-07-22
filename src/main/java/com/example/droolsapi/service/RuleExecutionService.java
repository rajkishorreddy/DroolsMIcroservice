package com.example.droolsapi.service;

import com.example.droolsapi.model.RuleExecutionRequest;
import com.example.droolsapi.service.dto.RuleExecutionResult;

public interface RuleExecutionService {
  RuleExecutionResult runRulesOnAllData(RuleExecutionRequest request);
}
