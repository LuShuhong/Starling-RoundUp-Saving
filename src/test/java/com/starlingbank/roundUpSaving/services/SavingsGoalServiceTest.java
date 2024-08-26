package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.configs.HeaderConfig;
import com.starlingbank.roundUpSaving.models.account.Account;
import com.starlingbank.roundUpSaving.models.savingsgoals.*;
import com.starlingbank.roundUpSaving.utils.UrlBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SavingsGoalServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private HeaderConfig headerConfig;
    @Mock
    private UrlBuilder urlBuilder;
    @InjectMocks
    private SavingsGoalService savingsGoalService;

    @Mock
    private Account mockAccount;

    @Mock
    private SavingGoalsRequest mockRequest;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        headerConfig = mock(HeaderConfig.class);
        urlBuilder = mock(UrlBuilder.class);
        mockAccount = mock(Account.class);
        mockRequest = mock(SavingGoalsRequest.class);

        savingsGoalService = new SavingsGoalService(restTemplate, headerConfig, urlBuilder);
    }

    @Test
    void testCreateNewSavingGoal() {
        // Arrange
        CreateSavingGoalResponse mockResponse = mock(CreateSavingGoalResponse.class);

        when(mockAccount.accountUid()).thenReturn("i-love-starling-and-coding");
        when(headerConfig.constructHeader()).thenReturn(null);
        when(urlBuilder.buildUrl(anyString(), anyString(), anyString())).thenReturn("i-love-starling-and-coding");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(CreateSavingGoalResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        CreateSavingGoalResponse result = savingsGoalService.createNewSavingGoal(mockAccount, mockRequest);

        // Assert
        assertEquals(mockResponse, result);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(CreateSavingGoalResponse.class));
    }

    @Test
    void testTransferToSavingsGoal() {
        // Arrange
        SavingsGoalTransferRequest mockRequest = mock(SavingsGoalTransferRequest.class);
        SavingsGoalTransferResponse mockResponse = mock(SavingsGoalTransferResponse.class);

        when(mockAccount.accountUid()).thenReturn("i-love-starling-and-coding");
        when(headerConfig.constructHeader()).thenReturn(null);
        when(urlBuilder.buildUrl(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("i-love-starling-and-coding");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(SavingsGoalTransferResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Act
        SavingsGoalTransferResponse result = savingsGoalService.transferToSavingsGoal(mockAccount, mockRequest, "i-love-starling-and-coding");

        // Assert
        assertEquals(mockResponse, result);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(SavingsGoalTransferResponse.class));
    }

}
