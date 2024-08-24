package com.starlingbank.roundUpSaving.controller;

import com.starlingbank.roundUpSaving.model.AccountsList;
import com.starlingbank.roundUpSaving.model.CreateSavingGoalResponse;
import com.starlingbank.roundUpSaving.model.FeedItemsList;
import com.starlingbank.roundUpSaving.model.SavingGoalsRequest;
import com.starlingbank.roundUpSaving.services.AccountService;
import com.starlingbank.roundUpSaving.services.SavingsGoalService;
import com.starlingbank.roundUpSaving.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/starling/v1")
public class RoundUpController {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final SavingsGoalService savingsGoalService;

    public RoundUpController(AccountService accountService, TransactionService transactionService, SavingsGoalService savingsGoalService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.savingsGoalService = savingsGoalService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<AccountsList> getAccountsList() {
        return accountService.getAccountsList();
    }

    @GetMapping("/transactions")
    public ResponseEntity<FeedItemsList> getFeedItemsList() {
        return transactionService.getWeeklyTransactions();
    }

    @PutMapping("/create-saving-goals")
    public ResponseEntity<CreateSavingGoalResponse> createSavingGoals(@RequestBody final SavingGoalsRequest savingGoalsRequest) {
        return savingsGoalService.createSavingGoal(savingGoalsRequest);
    }
}
