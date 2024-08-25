package com.starlingbank.roundUpSaving.model;

import lombok.Builder;

@Builder
public record SavingsGoalTransferRequest(Amount amount) {
}