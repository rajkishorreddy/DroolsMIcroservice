package com.example.droolsapi.service.dto;

public class RuleExecutionResult {
  private long totalProcessed;
  private long successCount;
  private long failureCount;

  public RuleExecutionResult() {
  }

  public RuleExecutionResult(long totalProcessed, long successCount, long failureCount) {
    this.totalProcessed = totalProcessed;
    this.successCount = successCount;
    this.failureCount = failureCount;
  }

  public long getTotalProcessed() {
    return totalProcessed;
  }

  public void setTotalProcessed(long totalProcessed) {
    this.totalProcessed = totalProcessed;
  }

  public long getSuccessCount() {
    return successCount;
  }

  public void setSuccessCount(long successCount) {
    this.successCount = successCount;
  }

  public long getFailureCount() {
    return failureCount;
  }

  public void setFailureCount(long failureCount) {
    this.failureCount = failureCount;
  }
}
