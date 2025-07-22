package com.example.droolsapi.model;

import java.util.List;

public class RuleExecutionRequest {
  private List<String> ruleNames;

  public List<String> getRuleNames() {
    return ruleNames;
  }

  public void setRuleNames(List<String> ruleNames) {
    this.ruleNames = ruleNames;
  }
}
