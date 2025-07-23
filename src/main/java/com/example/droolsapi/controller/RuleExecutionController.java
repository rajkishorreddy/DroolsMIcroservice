package com.example.droolsapi.controller;

import com.example.droolsapi.service.RuleExecutionService;
import com.example.droolsapi.service.dto.RuleExecutionRequest;
import com.example.droolsapi.service.dto.RuleExecutionResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class RuleExecutionController {

  private static final Logger logger = LoggerFactory.getLogger(RuleExecutionController.class);

  @Autowired
  private RuleExecutionService ruleExecutionService;

  @PostMapping("/run-rules")
  public ResponseEntity<RuleExecutionResult> runRules(@RequestBody RuleExecutionRequest request) {
    logger.info("[{}] Rule execution requested for rules: {}", LocalDateTime.now(), request.getRuleNames());

    RuleExecutionResult result = ruleExecutionService.runRulesOnAllData(request.getRuleNames());

    logger.info("[{}] Rule execution completed. Processed: {}, Success: {}, Failed: {}",
            LocalDateTime.now(),
            result.getTotalProcessed(),
            result.getSuccessCount(),
            result.getFailureCount());

    return ResponseEntity.ok(result);
  }

  @GetMapping("/health")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("✅ Drools API is running");
  }
}
