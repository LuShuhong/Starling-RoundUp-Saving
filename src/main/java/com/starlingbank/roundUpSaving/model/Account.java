package com.starlingbank.roundUpSaving.model;

public record Account(String accountUid,
                      String accountType,
                      String defaultCategory,
                      String currency,
                      String createdAt,
                      String name) {
}
