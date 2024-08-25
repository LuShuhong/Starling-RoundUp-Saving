package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.configs.HeaderConfig;
import com.starlingbank.roundUpSaving.exceptions.SavingsGoalServiceException;
import com.starlingbank.roundUpSaving.models.*;
import com.starlingbank.roundUpSaving.models.account.Account;
import com.starlingbank.roundUpSaving.models.savingsgoals.*;
import com.starlingbank.roundUpSaving.utils.UrlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SavingsGoalService {
    @Value("${starling.sandbox.host}")
    private String baseUrl;
    private final RestTemplate restTemplate;
    private final HeaderConfig headerConfig;
    private final UrlBuilder urlBuilder;

    public SavingsGoalService(RestTemplate restTemplate,
                              HeaderConfig headerConfig, UrlBuilder urlBuilder) {
        this.restTemplate = restTemplate;
        this.headerConfig = headerConfig;
        this.urlBuilder = urlBuilder;
    }

    public CreateSavingGoalResponse createNewSavingGoal(Account account, SavingGoalsRequest savingGoalsRequest) {
        String accountUid = account.accountUid();

        HttpEntity<SavingGoalsRequest> httpEntity = new HttpEntity<>(savingGoalsRequest, headerConfig.constructHeader());
        String endpoint = urlBuilder.buildUrl("account", accountUid, "savings-goals");

        try {
            return restTemplate.exchange(endpoint,
                    HttpMethod.PUT,
                    httpEntity,
                    CreateSavingGoalResponse.class).getBody();
        } catch (RestClientException e) {
            log.error("Failed to create savings goal for accountUid: {}. Error: {}", accountUid, e.getMessage(), e);
            throw new SavingsGoalServiceException("Unable to create savings goal", e);
        }
    }

    public SavingsGoal getDefaultSavingsGoal(Account account) {
        List<SavingsGoal> savingsGoals = getAllSavingsGoals(account).savingsGoalList();

        if (savingsGoals.isEmpty()) {
            log.debug("No savings goal exists for accountUid: {}", account.accountUid());
            String savingsGoalUid = createNewSavingGoal(account, buildDefaultSavingsGoalsRequest()).savingsGoalUid();
            return getSavingsGoalByUid(account, savingsGoalUid);
        }

        return savingsGoals.get(0);
    }

    private GetSavingsGoalsResponse getAllSavingsGoals(Account account) {
        String accountUid = account.accountUid();

        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
        String endpoint = urlBuilder.buildUrl("account", accountUid, "savings-goals");

        try {
            return restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, GetSavingsGoalsResponse.class).getBody();
        } catch (RestClientException e) {
            log.error("Failed to get all savings goals for accountUid: {}. Error: {}", accountUid, e.getMessage(), e);
            throw new SavingsGoalServiceException("Unable to get all savings goal", e);
        }

    }

    public SavingsGoal getSavingsGoalByUid(Account account, String savingsGoalUid) {
        String accountUid = account.accountUid();

        HttpEntity<Void> httpEntity = new HttpEntity<>(headerConfig.constructHeader());
        String endpoint = urlBuilder.buildUrl("account", accountUid, "savings-goals", savingsGoalUid);

        try {
            return restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, SavingsGoal.class).getBody();
        } catch (RestClientException e) {
            log.error("Failed to get savings goal: {} for accountUid: {}. Error: {}", savingsGoalUid, accountUid, e.getMessage(), e);
            throw new SavingsGoalServiceException("Unable to get savings goal by its UID", e);
        }
    }

    public SavingsGoalTransferResponse transferToSavingsGoal(Account account, SavingsGoalTransferRequest request, String savingsGoalUid) {
        String accountUid = account.accountUid();
        String uuid = UUID.randomUUID().toString();

        HttpEntity<SavingsGoalTransferRequest> httpEntity = new HttpEntity<>(request, headerConfig.constructHeader());
        String endpoint = urlBuilder.buildUrl("account",
                accountUid,
                "savings-goals",
                savingsGoalUid,
                "add-money",
                uuid);

        try{
            return restTemplate.exchange(endpoint,
                    HttpMethod.PUT,
                    httpEntity,
                    SavingsGoalTransferResponse.class).getBody();
        }  catch (RestClientException e) {
            log.error("Failed to transfer to savings goal: {} for accountUid: {}. Error: {}", savingsGoalUid, accountUid, e.getMessage(), e);
            throw new SavingsGoalServiceException("Transfer to savings goal failed", e);
        }
    }

    private SavingGoalsRequest buildDefaultSavingsGoalsRequest() {
        return SavingGoalsRequest.builder()
                .name("default")
                .target(new Amount("GBP", 102400))
                .currency("GBP")
                .build();
    }
}
