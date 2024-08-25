package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.models.*;
import com.starlingbank.roundUpSaving.models.account.Account;
import com.starlingbank.roundUpSaving.models.savingsgoals.SavingsGoalTransferRequest;
import com.starlingbank.roundUpSaving.models.savingsgoals.SavingsGoalTransferResponse;
import com.starlingbank.roundUpSaving.models.transactions.FeedItemsList;
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
