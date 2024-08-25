package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.configs.HeaderConfig;
import com.starlingbank.roundUpSaving.models.account.Account;
import com.starlingbank.roundUpSaving.models.account.AccountsList;
import com.starlingbank.roundUpSaving.utils.UrlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Slf4j
@Service
public class AccountService {
    private final RestTemplate restTemplate;
    private final HeaderConfig headerConfig;
    private final UrlBuilder urlBuilder;


    public AccountService(RestTemplate restTemplate, HeaderConfig headerConfig, UrlBuilder urlBuilder) {
        this.restTemplate = restTemplate;
        this.headerConfig = headerConfig;
        this.urlBuilder = urlBuilder;
    }

    public ResponseEntity<AccountsList> getAccountsList() {
        String url = urlBuilder.buildUrl("accounts");
        try {
            HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, AccountsList.class);
        } catch (RestClientException e) {
            log.error("Failed to retrieve accounts", e);
            throw e;
        }
    }

    public Account getDefaultAccount() {
        return Objects.requireNonNull(getAccountsList().getBody())
                .accounts()
                .get(0);
    }
}
