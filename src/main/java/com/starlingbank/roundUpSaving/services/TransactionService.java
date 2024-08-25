package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.config.HeaderConfig;
import com.starlingbank.roundUpSaving.model.account.Account;
import com.starlingbank.roundUpSaving.model.transactions.FeedItem;
import com.starlingbank.roundUpSaving.model.transactions.FeedItemsList;
import com.starlingbank.roundUpSaving.model.transactions.TransactionDirection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class TransactionService {

    @Value("${starling.sandbox.host}")
    private String baseUrl;
    private final RestTemplate restTemplate;
    private final HeaderConfig headerConfig;

    public TransactionService(RestTemplate restTemplate,
                              HeaderConfig headerConfig) {
        this.restTemplate = restTemplate;
        this.headerConfig = headerConfig;
    }

    public FeedItemsList getWeeklyTransactions(Account account) {
        String accountUid = account.accountUid();
        String categoryUid = account.defaultCategory();
        Instant aWeekAgo = Instant.now().minus(Duration.ofDays(7));
        String isoDate = DateTimeFormatter.ISO_INSTANT.format(aWeekAgo);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
        String endpoint = String.format("%s/api/v2/feed/account/%s/category/%s?changesSince=%s",
                baseUrl,
                accountUid,
                categoryUid,
                isoDate);
        try {
            return restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, FeedItemsList.class).getBody();
        } catch (RestClientException e) {
            log.error("Failed to retrieve transactions for accountUid: {}, categoryUid: {}. Endpoint: {}. Error: {}",
                    accountUid, categoryUid, endpoint, e.getMessage(), e);
            throw e;
        }
    }

    public int getTotalRoundUpFromTransactions(FeedItemsList feedItemsList) {
        return feedItemsList.feedItems().stream()
                .filter(this::isOutgoingTransaction)
                .mapToInt(this::calculateRoundUp)
                .sum();
    }

    private boolean isOutgoingTransaction(FeedItem transaction) {
        return transaction.direction().equals(TransactionDirection.OUT.name());
    }

    private int calculateRoundUp(FeedItem transaction) {
        int minorUnits = transaction.amount().minorUnits();
        int remainder = minorUnits % 100;
        return (remainder == 0) ? 0 : 100 - remainder;
    }
}