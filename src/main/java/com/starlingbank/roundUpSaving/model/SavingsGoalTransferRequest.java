package com.starlingbank.roundUpSaving.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavingsGoalTransferRequest {
    @NonNull
    Amount amount;
}