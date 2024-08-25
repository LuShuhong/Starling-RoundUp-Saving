package com.starlingbank.roundUpSaving.models.savingsgoals;

import com.starlingbank.roundUpSaving.models.Amount;
import lombok.Builder;

@Builder
public record SavingsGoalTransferRequest(Amount amount) {
}