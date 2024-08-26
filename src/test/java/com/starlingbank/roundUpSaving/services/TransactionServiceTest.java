package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.configs.HeaderConfig;
import com.starlingbank.roundUpSaving.models.Amount;
import com.starlingbank.roundUpSaving.models.account.Account;
import com.starlingbank.roundUpSaving.models.transactions.FeedItem;
import com.starlingbank.roundUpSaving.models.transactions.FeedItemsList;
import com.starlingbank.roundUpSaving.models.transactions.TransactionDirection;
import com.starlingbank.roundUpSaving.utils.UrlBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        restTemplate = mock(RestTemplate.class);
        headerConfig = mock(HeaderConfig.class);
        urlBuilder = mock(UrlBuilder.class);
        transactionService = new TransactionService(restTemplate, headerConfig, urlBuilder);
    }

    @Test
    void testGetWeeklyTransactions() {
        Account mockAccount = mock(Account.class);
        FeedItemsList mockFeedItemsList = mock(FeedItemsList.class);
        ResponseEntity<FeedItemsList> mockResponse = mock(ResponseEntity.class);

        when(mockAccount.accountUid()).thenReturn("mock-account-uid");
        when(mockAccount.defaultCategory()).thenReturn("mock-category-uid");
        when(headerConfig.constructHeader()).thenReturn(null);
        when(urlBuilder.buildUrl(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn("mock-url");
        when(urlBuilder.buildParam(anyString(), anyString(), anyString())).thenReturn("mock-url-with-param");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(FeedItemsList.class)))
                .thenReturn(mockResponse);
        when(mockResponse.getBody()).thenReturn(mockFeedItemsList);

        // Act
        FeedItemsList result = transactionService.getWeeklyTransactions(mockAccount);

        // Assert
        assertEquals(mockFeedItemsList, result);
        verify(restTemplate).exchange(eq("mock-url-with-param"), eq(HttpMethod.GET), any(HttpEntity.class), eq(FeedItemsList.class));
    }
    @Test
    void testGetTotalRoundUpFromTransactions() {
        // Arrange
        FeedItem mockTransaction1 = mock(FeedItem.class);
        FeedItem mockTransaction2 = mock(FeedItem.class);
        FeedItemsList mockFeedItemsList = mock(FeedItemsList.class);

        when(mockFeedItemsList.feedItems()).thenReturn(List.of(mockTransaction1, mockTransaction2));
        when(mockTransaction1.direction()).thenReturn(TransactionDirection.OUT.name());
        when(mockTransaction2.direction()).thenReturn(TransactionDirection.OUT.name());
        when(mockTransaction1.amount()).thenReturn(new Amount("GBP", 123));
        when(mockTransaction2.amount()).thenReturn(new Amount("GBP", 456));

        // Act
        int result = transactionService.getTotalRoundUpFromTransactions(mockFeedItemsList);

        // Assert
        assertEquals((100 - 123 % 100) + (100 - 456 % 100), result);
    }

    @Test
    void testIsOutgoingTransaction() {
        // Arrange
        FeedItem mockTransaction = mock(FeedItem.class);
        when(mockTransaction.direction()).thenReturn(TransactionDirection.OUT.name());

        // Act
        boolean result = transactionService.isOutgoingTransaction(mockTransaction);

        // Assert
        assertTrue(result);
    }
}