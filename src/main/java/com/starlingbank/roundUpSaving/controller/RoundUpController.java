package com.starlingbank.roundUpSaving.controller;

import com.starlingbank.roundUpSaving.model.*;
import com.starlingbank.roundUpSaving.services.AccountService;
import com.starlingbank.roundUpSaving.services.RoundUpService;
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
    private final RoundUpService roundUpService;

    public RoundUpController(AccountService accountService,
                             TransactionService transactionService,
                             SavingsGoalService savingsGoalService,
                             RoundUpService roundUpService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.savingsGoalService = savingsGoalService;
        this.roundUpService = roundUpService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<AccountsList> getAccountsList() {
        return accountService.getAccountsList();
    }

    @GetMapping("/transactions")
    public ResponseEntity<FeedItemsList> getFeedItemsList() {
        return ResponseEntity.ok(transactionService.getWeeklyTransactions());
    }

    @GetMapping("/savings-goals")
    public ResponseEntity<GetSavingsGoalsResponse> getAllSavingsGoals() {
        return ResponseEntity.ok(savingsGoalService.getAllSavingGoals());
    }

    @GetMapping("/savings-goals/{id}")
    public ResponseEntity<SavingsGoal> getSavingsGoalByUid(@PathVariable("id") String uid) {
        return ResponseEntity.ok(savingsGoalService.getSavingsGoalByUid(uid));
    }

    @PutMapping("/create-saving-goals")
    public ResponseEntity<CreateSavingGoalResponse> createSavingGoals(@RequestBody SavingGoalsRequest savingGoalsRequest) {
        return ResponseEntity.ok(savingsGoalService.createNewSavingGoal(savingGoalsRequest));
    }

    @PutMapping("/transfer-money")
    public ResponseEntity<SavingsGoalTransferResponse> transfer(@RequestBody SavingsGoalTransferRequest savingsGoalTransferRequest,
                                                                @RequestParam String savingsGoalUid) {
        return ResponseEntity.ok(savingsGoalService.transferToSavingsGoal(savingsGoalTransferRequest, savingsGoalUid));
    }

    @GetMapping("/save-my-weekly-round-up")
    public ResponseEntity<SavingsGoalTransferResponse> saveRoundUp() {
        return ResponseEntity.ok(roundUpService.executeRoundUp());
    }
}
