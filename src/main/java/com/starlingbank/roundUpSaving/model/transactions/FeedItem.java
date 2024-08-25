package com.starlingbank.roundUpSaving.model.transactions;

import com.starlingbank.roundUpSaving.model.Amount;

public record FeedItem(String uid, Amount amount, String direction) {
}
