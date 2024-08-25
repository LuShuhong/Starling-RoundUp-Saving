package com.starlingbank.roundUpSaving.controller;

import com.starlingbank.roundUpSaving.model.account.Account;
import com.starlingbank.roundUpSaving.model.savingsgoals.SavingsGoalTransferResponse;
import com.starlingbank.roundUpSaving.services.AccountService;
import com.starlingbank.roundUpSaving.services.RoundUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/starling/v1")
public class RoundUpController {
    private final AccountService accountService;
    private final RoundUpService roundUpService;

    public RoundUpController(AccountService accountService,
                             RoundUpService roundUpService) {
        this.accountService = accountService;
        this.roundUpService = roundUpService;
    }

    @PutMapping("/save-my-weekly-round-up")
    public ResponseEntity<SavingsGoalTransferResponse> saveWeeklyRoundUp() {
        Account account = accountService.getDefaultAccount();
        return ResponseEntity.ok(roundUpService.executeRoundUp(account));
    }
}
