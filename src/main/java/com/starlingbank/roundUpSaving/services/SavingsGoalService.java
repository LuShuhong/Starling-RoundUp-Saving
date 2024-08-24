package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.config.HeaderConfig;
import com.starlingbank.roundUpSaving.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SavingsGoalService {
    @Value("${starling.sandbox.host}")
    private String baseUrl;
    private final RestTemplate restTemplate;
    private final HeaderConfig headerConfig;
    private final Account account;
    private String accountUid;

    public SavingsGoalService(RestTemplate restTemplate,
                              HeaderConfig headerConfig,
                              Account account) {
        this.restTemplate = restTemplate;
        this.headerConfig = headerConfig;
        this.account = account;
    }

    public CreateSavingGoalResponse createSavingGoal(SavingGoalsRequest savingGoalsRequest) {
        accountUid = account.accountUid();
        SavingGoalsRequest requestBody = buildSavingGoalRequest(savingGoalsRequest);
        HttpEntity<SavingGoalsRequest> httpEntity = new HttpEntity<>(requestBody, headerConfig.constructHeader());
        String endpoint = String.format("%s/api/v2/account/%s/savings-goals", baseUrl, accountUid);
        return restTemplate.exchange(endpoint,
                HttpMethod.PUT,
                httpEntity,
                CreateSavingGoalResponse.class)
                .getBody();
    }

    public GetSavingsGoalsResponse getAllSavingGoals() {
        accountUid = account.accountUid();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
        String endpoint = baseUrl + "/api/v2/account/" + accountUid+ "/savings-goals";
        return restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, GetSavingsGoalsResponse.class).getBody();
    }

    public SavingsGoal getSavingsGoalByUid(String savingsGoalUid) {
        accountUid = account.accountUid();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
        String endpoint = baseUrl + "/api/v2/account/" + accountUid + "/savings-goals/" + savingsGoalUid;
        return restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, SavingsGoal.class).getBody();
    }

    private SavingGoalsRequest buildSavingGoalRequest(SavingGoalsRequest savingGoalsRequest) {
        return SavingGoalsRequest.builder()
                .name(savingGoalsRequest.getName())
                .currency(savingGoalsRequest.getCurrency())
                .target(new Amount(savingGoalsRequest.getTarget().currency(),
                        savingGoalsRequest.getTarget().minorUnits()))
                .build();
    }




}
