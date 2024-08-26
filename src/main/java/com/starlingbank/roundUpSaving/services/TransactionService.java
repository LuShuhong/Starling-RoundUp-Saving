package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.configs.HeaderConfig;
import com.starlingbank.roundUpSaving.models.account.Account;
import com.starlingbank.roundUpSaving.models.transactions.FeedItem;
import com.starlingbank.roundUpSaving.models.transactions.FeedItemsList;
import com.starlingbank.roundUpSaving.models.transactions.TransactionDirection;
import com.starlingbank.roundUpSaving.utils.UrlBuilder;
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
    private final UrlBuilder urlBuilder;

    public TransactionService(RestTemplate restTemplate,
                              HeaderConfig headerConfig, UrlBuilder urlBuilder) {
        this.restTemplate = restTemplate;
        this.headerConfig = headerConfig;
        this.urlBuilder = urlBuilder;
    }

    public FeedItemsList getWeeklyTransactions(Account account) {
        String accountUid = account.accountUid();
        String categoryUid = account.defaultCategory();
        Instant aWeekAgo = Instant.now().minus(Duration.ofDays(7));
        String isoDate = DateTimeFormatter.ISO_INSTANT.format(aWeekAgo);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());

        String url = urlBuilder.buildUrl("feed",
                "account",
                accountUid,
                "category",
                categoryUid);
        String urlWithParam = urlBuilder.buildParam(url, "changesSince", isoDate);

        try {
            return restTemplate.exchange(urlWithParam, HttpMethod.GET, httpEntity, FeedItemsList.class).getBody();
        } catch (RestClientException e) {
            log.error("Failed to retrieve transactions for accountUid: {}, categoryUid: {}. Endpoint: {}. Error: {}",
                    accountUid, categoryUid, urlWithParam, e.getMessage(), e);
            throw e;
        }
    }

    public int getTotalRoundUpFromTransactions(FeedItemsList feedItemsList) {
        return feedItemsList.feedItems().stream()
                .filter(this::isOutgoingTransaction)
                .mapToInt(this::calculateRoundUp)
                .sum();
    }

    boolean isOutgoingTransaction(FeedItem transaction) {
        return transaction.direction().equals(TransactionDirection.OUT.name());
    }

    int calculateRoundUp(FeedItem transaction) {
        int minorUnits = transaction.amount().minorUnits();
        int remainder = minorUnits % 100;
        return (remainder == 0) ? 0 : 100 - remainder;
    }
}