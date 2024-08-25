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

    public SavingsGoalService(RestTemplate restTemplate,
                              HeaderConfig headerConfig) {
        this.restTemplate = restTemplate;
        this.headerConfig = headerConfig;
    }

    public CreateSavingGoalResponse createNewSavingGoal(Account account, SavingGoalsRequest savingGoalsRequest) {
        String accountUid = account.accountUid();
        HttpEntity<SavingGoalsRequest> httpEntity = new HttpEntity<>(savingGoalsRequest, headerConfig.constructHeader());
        String endpoint = String.format("%s/api/v2/account/%s/savings-goals", baseUrl, accountUid);
        return restTemplate.exchange(endpoint,
                HttpMethod.PUT,
                httpEntity,
                CreateSavingGoalResponse.class).getBody();
    }

    public GetSavingsGoalsResponse getAllSavingGoals(Account account) {
        String accountUid = account.accountUid();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
        String endpoint = baseUrl + "/api/v2/account/" + accountUid+ "/savings-goals";
        return restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, GetSavingsGoalsResponse.class).getBody();
    }

    public SavingsGoal getSavingsGoalByUid(Account account, String savingsGoalUid) {
        String accountUid = account.accountUid();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
        String endpoint = baseUrl + "/api/v2/account/" + accountUid + "/savings-goals/" + savingsGoalUid;
        return restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, SavingsGoal.class).getBody();
    }

    public SavingsGoal getDefaultSavingsGoal(Account account) {
        return getAllSavingGoals(account).savingsGoalList().get(0);
    }

    public SavingsGoalTransferResponse transferToSavingsGoal(Account account, SavingsGoalTransferRequest request, String savingsGoalUid) {
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
