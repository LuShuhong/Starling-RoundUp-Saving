package com.starlingbank.roundUpSaving.models.savingsgoals;

import com.starlingbank.roundUpSaving.models.Amount;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavingGoalsRequest {
    @NonNull
    private String name;
    @NonNull
    private String currency;
    @NonNull
    private Amount target;
    private String base64EncodedPhoto;
}
