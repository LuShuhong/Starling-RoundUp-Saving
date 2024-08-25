package com.starlingbank.roundUpSaving.models.account;

public record Account(String accountUid,
                      String accountType,
                      String defaultCategory,
                      String currency,
                      String createdAt,
                      String name) {
}
