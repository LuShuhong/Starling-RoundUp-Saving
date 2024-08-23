package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.config.HeaderConfig;
import com.starlingbank.roundUpSaving.model.AccountsList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class AccountService {

    @Value("${starling.sandbox.host}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final HeaderConfig headerConfig;

    public AccountService(RestTemplate restTemplate, HeaderConfig headerConfig) {
        this.restTemplate = restTemplate;
        this.headerConfig = headerConfig;
    }

public ResponseEntity<AccountsList> getAccountList() {
    HttpEntity<AccountsList> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
    return restTemplate.exchange(baseUrl + "/api/v2/accounts", HttpMethod.GET, httpEntity, AccountsList.class);
}



}
