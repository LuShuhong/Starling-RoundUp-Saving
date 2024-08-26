package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.configs.HeaderConfig;
import com.starlingbank.roundUpSaving.models.Amount;
import com.starlingbank.roundUpSaving.models.transactions.FeedItem;
import com.starlingbank.roundUpSaving.models.transactions.FeedItemsList;
import com.starlingbank.roundUpSaving.utils.UrlBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private HeaderConfig headerConfig;
    @Mock
    private UrlBuilder urlBuilder;
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(restTemplate, headerConfig, urlBuilder);

    }

    @Test
    void testGetTotalRoundUpFromTransactions() {
        // Arrange
        Amount amount = new Amount("GBP", 123);
        FeedItem mockTransaction = new FeedItem("uid", amount, "OUT");
        FeedItemsList mockFeedItemsList = new FeedItemsList(List.of(mockTransaction, mockTransaction));

        // Act
        int result = transactionService.getTotalRoundUpFromTransactions(mockFeedItemsList);

        // Assert
        assertEquals((100 - 123 % 100) + (100 - 123 % 100), result);
    }

    @Test
    void testIsOutgoingTransaction() {
        //Arrange
        Amount amount = new Amount("GBP", 123);
        FeedItem mockTransaction = new FeedItem("uid", amount, "OUT");

        // Act
        boolean result = transactionService.isOutgoingTransaction(mockTransaction);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCalculateRoundUp() {
        // Arrange
        Amount amount = new Amount("GBP", 123);
        FeedItem mockTransaction = new FeedItem("uid", amount, "OUT");

        //Act
        int result = transactionService.calculateRoundUp(mockTransaction);

        // Act and Assert
        assertEquals(77, result);
    }
}