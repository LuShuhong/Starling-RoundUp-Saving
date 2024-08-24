package com.starlingbank.roundUpSaving.controller;

import com.starlingbank.roundUpSaving.model.AccountsList;
import com.starlingbank.roundUpSaving.model.FeedItemsList;
import com.starlingbank.roundUpSaving.services.AccountService;
import com.starlingbank.roundUpSaving.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/starling/v1")
public class RoundUpController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public RoundUpController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<AccountsList> getAccountsList() {
        return accountService.getAccountsList();
    }

    @GetMapping("/transactions")
    public ResponseEntity<FeedItemsList> getFeedItemsList() {
        return transactionService.getWeeklyTransactions();
    }
}
