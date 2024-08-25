package com.starlingbank.roundUpSaving.model.savingsgoals;

import com.starlingbank.roundUpSaving.model.Amount;
import lombok.Builder;

@Builder
public record SavingsGoalTransferRequest(Amount amount) {
}