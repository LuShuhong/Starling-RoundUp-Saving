package com.starlingbank.roundUpSaving.models.savingsgoals;

import com.starlingbank.roundUpSaving.models.Amount;

public record SavingsGoal(String savingsGoalUid,
                          String name,
                          Amount target,
                          Amount totalSaved,
                          int savedPercentage,
                          String state) {
}
