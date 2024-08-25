package com.starlingbank.roundUpSaving.controller;

import com.starlingbank.roundUpSaving.model.*;
import com.starlingbank.roundUpSaving.services.AccountService;
import com.starlingbank.roundUpSaving.services.RoundUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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

    @GetMapping("/save-my-weekly-round-up")
    public ResponseEntity<SavingsGoalTransferResponse> saveRoundUp() {
        Account account = Objects.requireNonNull(accountService.getAccountsList().getBody())
                .accounts()
                .get(0);
        return ResponseEntity.ok(roundUpService.executeRoundUp(account));
    }
}
