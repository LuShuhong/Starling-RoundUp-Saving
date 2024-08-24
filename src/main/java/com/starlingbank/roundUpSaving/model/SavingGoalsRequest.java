package com.starlingbank.roundUpSaving.model;

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
