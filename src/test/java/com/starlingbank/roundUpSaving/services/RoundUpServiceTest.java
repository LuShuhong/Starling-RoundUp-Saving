package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.models.account.Account;
import com.starlingbank.roundUpSaving.models.savingsgoals.SavingsGoal;
import com.starlingbank.roundUpSaving.models.savingsgoals.SavingsGoalTransferRequest;
import com.starlingbank.roundUpSaving.models.savingsgoals.SavingsGoalTransferResponse;
import com.starlingbank.roundUpSaving.models.transactions.FeedItemsList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoundUpServiceTest {
    @Mock
    private TransactionService transactionService;

    @Mock
    private SavingsGoalService savingsGoalService;

    @InjectMocks
    private RoundUpService roundUpService;

    @Mock
    private Account mockAccount;

    @Mock
    private FeedItemsList mockTransactions;

    @Mock
    private SavingsGoal mockSavingsGoal;

    @BeforeEach
    void setUp() {
        transactionService = mock(TransactionService.class);
        savingsGoalService = mock(SavingsGoalService.class);
        roundUpService = new RoundUpService(transactionService, savingsGoalService);
        mockTransactions = mock(FeedItemsList.class);
        mockSavingsGoal = mock(SavingsGoal.class);
        when(transactionService.getWeeklyTransactions(mockAccount)).thenReturn(mockTransactions);
        when(savingsGoalService.getDefaultSavingsGoal(mockAccount)).thenReturn(mockSavingsGoal);
        when(mockSavingsGoal.savingsGoalUid()).thenReturn("starling-savings-goal");
    }

    @Test
    void testExecuteRoundUpSuccess() {
        // Arrange
        SavingsGoalTransferResponse mockResponse = mock(SavingsGoalTransferResponse.class);

        when(transactionService.getTotalRoundUpFromTransactions(mockTransactions)).thenReturn(1234);
        when(savingsGoalService.transferToSavingsGoal(eq(mockAccount), any(SavingsGoalTransferRequest.class), eq("starling-savings-goal"))).thenReturn(mockResponse);

        // Act
        SavingsGoalTransferResponse result = roundUpService.executeRoundUp(mockAccount);

        // Assert
        assertEquals(mockResponse, result);
        verify(transactionService).getWeeklyTransactions(mockAccount);
        verify(transactionService).getTotalRoundUpFromTransactions(mockTransactions);
        verify(savingsGoalService).getDefaultSavingsGoal(mockAccount);
        verify(savingsGoalService).transferToSavingsGoal(eq(mockAccount), any(SavingsGoalTransferRequest.class), eq("starling-savings-goal"));
    }

    @Test
    void testExecuteRoundUpFailure() {
        // Arrange
        when(transactionService.getTotalRoundUpFromTransactions(mockTransactions)).thenReturn(1234);
        when(savingsGoalService.transferToSavingsGoal(eq(mockAccount), any(SavingsGoalTransferRequest.class), eq("starling-savings-goal"))).thenThrow(new RuntimeException("Transfer failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> roundUpService.executeRoundUp(mockAccount));
        verify(transactionService).getWeeklyTransactions(mockAccount);
        verify(transactionService).getTotalRoundUpFromTransactions(mockTransactions);
        verify(savingsGoalService).getDefaultSavingsGoal(mockAccount);
        verify(savingsGoalService).transferToSavingsGoal(eq(mockAccount), any(SavingsGoalTransferRequest.class), eq("starling-savings-goal"));
    }

    @Test
    void testExecuteRoundUpNoTransactions() {
        // Arrange
        SavingsGoalTransferResponse mockResponse = mock(SavingsGoalTransferResponse.class);

        when(transactionService.getTotalRoundUpFromTransactions(mockTransactions)).thenReturn(0);
        when(savingsGoalService.transferToSavingsGoal(eq(mockAccount), any(SavingsGoalTransferRequest.class), eq("starling-savings-goal"))).thenReturn(mockResponse);

        // Act
        SavingsGoalTransferResponse result = roundUpService.executeRoundUp(mockAccount);

        // Assert
        assertEquals(mockResponse, result);
        verify(transactionService).getWeeklyTransactions(mockAccount);
        verify(transactionService).getTotalRoundUpFromTransactions(mockTransactions);
        verify(savingsGoalService).getDefaultSavingsGoal(mockAccount);
        verify(savingsGoalService).transferToSavingsGoal(eq(mockAccount), any(SavingsGoalTransferRequest.class), eq("starling-savings-goal"));
    }
}