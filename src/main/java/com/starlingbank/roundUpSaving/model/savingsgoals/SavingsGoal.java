package com.starlingbank.roundUpSaving.model.savingsgoals;

import com.starlingbank.roundUpSaving.model.Amount;

public record SavingsGoal(String savingsGoalUid,
                          String name,
                          Amount target,
                          Amount totalSaved,
                          int savedPercentage,
                          String state) {
}
