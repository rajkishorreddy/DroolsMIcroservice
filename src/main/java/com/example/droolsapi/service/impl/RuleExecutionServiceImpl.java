package com.example.droolsapi.service.impl;

import com.example.droolsapi.model.RuleExecutionRequest;
import com.example.droolsapi.service.RuleExecutionService;
import com.example.droolsapi.service.dto.RuleExecutionResult;
import com.example.droolsapi.util.DroolsHelper;
import com.example.droolsapi.util.FactsExtractor;
import com.example.droolsapi.util.ResultLoader;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleExecutionServiceImpl implements RuleExecutionService {

  private static final Logger logger = LoggerFactory.getLogger(RuleExecutionServiceImpl.class);

  private static final int ID_BATCH_SIZE = 1000;

  @Override
  public RuleExecutionResult runRulesOnAllData(RuleExecutionRequest request) {
    long totalProcessed = 0;
    long successCount = 0;
    long failureCount = 0;
    int offset = 0;

    while (true) {
      List<String> idBatch = FactsExtractor.loadUniqueIds(offset, ID_BATCH_SIZE);
      if (idBatch.isEmpty()) break;

      for (String id : idBatch) {
        try {
          List<Object> facts = FactsExtractor.loadFactsForId(id);
          KieSession session = DroolsHelper.buildKieSession(request.getRuleNames());

          facts.forEach(session::insert);
          session.fireAllRules();
          session.dispose();

          ResultLoader.saveResults(facts, id);  // Customize per your data model
          successCount++;
        } catch (Exception e) {
          logger.error("❌ Error processing ID {}: {}", id, e.getMessage());
          failureCount++;
        }
        totalProcessed++;
      }
      offset += ID_BATCH_SIZE;
    }

    return new RuleExecutionResult(totalProcessed, successCount, failureCount);
  }
}
