package com.starlingbank.roundUpSaving.models.transactions;

import com.starlingbank.roundUpSaving.models.Amount;

public record FeedItem(String uid, Amount amount, String direction) {
}
