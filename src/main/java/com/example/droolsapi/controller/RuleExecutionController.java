package com.example.droolsapi.controller;

import com.example.droolsapi.model.RuleExecutionRequest;
import com.example.droolsapi.service.RuleExecutionService;
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

  @PostMapping("/execute")
  public ResponseEntity<RuleExecutionResult> runRulesOnAllIds(@RequestBody RuleExecutionRequest request) {
    logger.info("[{}] Executing rules {} on all IDs", LocalDateTime.now(), request.getRuleNames());

    RuleExecutionResult result = ruleExecutionService.runRulesById(request);

    logger.info("[{}] Execution completed: total={}, success={}, failure={}",
            LocalDateTime.now(), result.getTotalProcessed(), result.getSuccessCount(), result.getFailureCount());

    return ResponseEntity.ok(result);
  }

  @GetMapping("/health")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("Drools Rule Executor is healthy");
  }
}
