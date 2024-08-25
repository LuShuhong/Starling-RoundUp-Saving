package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.config.HeaderConfig;
import com.starlingbank.roundUpSaving.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class SavingsGoalService {
    @Value("${starling.sandbox.host}")
    private String baseUrl;
    private final RestTemplate restTemplate;
    private final HeaderConfig headerConfig;
    private final Account account;

    public SavingsGoalService(RestTemplate restTemplate,
                              HeaderConfig headerConfig,
                              Account account) {
        this.restTemplate = restTemplate;
        this.headerConfig = headerConfig;
        this.account = account;
    }

    public CreateSavingGoalResponse createNewSavingGoal(SavingGoalsRequest savingGoalsRequest) {
        String accountUid = account.accountUid();
        HttpEntity<SavingGoalsRequest> httpEntity = new HttpEntity<>(savingGoalsRequest, headerConfig.constructHeader());
        String endpoint = String.format("%s/api/v2/account/%s/savings-goals", baseUrl, accountUid);
        return restTemplate.exchange(endpoint,
                HttpMethod.PUT,
                httpEntity,
                CreateSavingGoalResponse.class).getBody();
    }

    public GetSavingsGoalsResponse getAllSavingGoals() {
        String accountUid = account.accountUid();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
        String endpoint = baseUrl + "/api/v2/account/" + accountUid+ "/savings-goals";
        return restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, GetSavingsGoalsResponse.class).getBody();
    }

    public SavingsGoal getSavingsGoalByUid(String savingsGoalUid) {
        String accountUid = account.accountUid();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
        String endpoint = baseUrl + "/api/v2/account/" + accountUid + "/savings-goals/" + savingsGoalUid;
        return restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, SavingsGoal.class).getBody();
    }

    public SavingsGoal getDefaultSavingsGoal() {
        return getAllSavingGoals().savingsGoalList().get(0);
    }

    public SavingsGoalTransferResponse transferToSavingsGoal(SavingsGoalTransferRequest request, String savingsGoalUid) {
        String accountUid = account.accountUid();
        String uuid = UUID.randomUUID().toString();
        HttpEntity<SavingsGoalTransferRequest> httpEntity = new HttpEntity<>(request, headerConfig.constructHeader());
        String endpoint = String.format("%s/api/v2/account/%s/savings-goals/%s/add-money/%s",
                baseUrl,
                accountUid,
                savingsGoalUid,
                uuid);
        return restTemplate.exchange(endpoint,
                        HttpMethod.PUT,
                        httpEntity,
                        SavingsGoalTransferResponse.class).getBody();
    }
}
