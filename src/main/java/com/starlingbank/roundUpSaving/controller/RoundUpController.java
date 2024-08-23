package com.starlingbank.roundUpSaving.controller;

import com.starlingbank.roundUpSaving.model.AccountsList;
import com.starlingbank.roundUpSaving.services.AccountService;
import com.starlingbank.roundUpSaving.services.RoundUpService;
import com.starlingbank.roundUpSaving.services.TransactionFeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/starling/v1")
public class RoundUpController {
    private final AccountService accountService;

    public RoundUpController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<AccountsList> getAccountsList() {
        return accountService.getAccountList();
    }
}
