package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.model.*;
import org.springframework.stereotype.Service;

@Service
public class RoundUpService {

    private final TransactionService transactionService;
    private final SavingsGoalService savingsGoalService;
    public RoundUpService(TransactionService transactionService, SavingsGoalService savingsGoalService) {
        this.transactionService = transactionService;
        this.savingsGoalService = savingsGoalService;
    }

    public SavingsGoalTransferResponse executeRoundUp(Account account) {
        FeedItemsList transactions = transactionService.getWeeklyTransactions(account);
        int totalRoundUp = transactionService.getTotalRoundUpFromTransactions(transactions);
        String savingsGoalUid = savingsGoalService.getDefaultSavingsGoal(account).savingsGoalUid();
        SavingsGoalTransferRequest request = SavingsGoalTransferRequest.builder()
                .amount(new Amount("GBP", totalRoundUp))
                .build();

        return savingsGoalService.transferToSavingsGoal(account, request, savingsGoalUid);
    }

}
