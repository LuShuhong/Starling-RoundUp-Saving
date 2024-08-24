package com.starlingbank.roundUpSaving.model;

public record SavingsGoal(String savingsGoalUid,
                          String name,
                          Amount target,
                          Amount totalSaved,
                          int savedPercentage,
                          String state) {
}
