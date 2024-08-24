package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.config.HeaderConfig;
import com.starlingbank.roundUpSaving.model.Account;
import com.starlingbank.roundUpSaving.model.FeedItemsList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionService {

    @Value("${starling.sandbox.host}")
    private String baseUrl;
    private final RestTemplate restTemplate;
    private final HeaderConfig headerConfig;
    private final Account account;


    public TransactionService(RestTemplate restTemplate, HeaderConfig headerConfig, Account account) {
        this.restTemplate = restTemplate;
        this.headerConfig = headerConfig;
        this.account = account;
    }


    public ResponseEntity<FeedItemsList> getWeeklyTransactions() {
        String accountUid = account.accountUid();
        String categoryUid = account.defaultCategory();
        Instant aWeekAgo = Instant.now().minus(Duration.ofDays(7));
        String isoDate = DateTimeFormatter.ISO_INSTANT.format(aWeekAgo);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
        String endpoint = String.format("%s/api/v2/feed/account/%s/category/%s?changesSince=%s", baseUrl, accountUid, categoryUid, isoDate);
        return restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, FeedItemsList.class);
    }

}