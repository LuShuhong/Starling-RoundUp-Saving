package com.starlingbank.roundUpSaving.services;

import com.starlingbank.roundUpSaving.configs.HeaderConfig;
import com.starlingbank.roundUpSaving.models.account.Account;
import com.starlingbank.roundUpSaving.models.account.AccountsList;
import com.starlingbank.roundUpSaving.utils.UrlBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HeaderConfig headerConfig;

    @Mock
    private UrlBuilder urlBuilder;
    @InjectMocks
    private AccountService accountService;

    @Mock
    private Account mockAccount;

    @Mock
    private AccountsList mockAccountsList;

    @Mock
    private ResponseEntity<AccountsList> mockResponseEntity;

    @Mock
    private String mockUrl;

    @Mock
    private HttpHeaders mockHeaders;

    @BeforeEach
    //Arrange
    public void setup() {
        restTemplate = mock(RestTemplate.class);
        headerConfig = mock(HeaderConfig.class);
        urlBuilder = mock(UrlBuilder.class);
        accountService = new AccountService(restTemplate,headerConfig, urlBuilder);

        mockUrl = "I-love-starling-bank-and-this-challenge-is-interesting";
        mockHeaders = new HttpHeaders();
        mockAccount = new Account("accUid", "type", "category", "GBP", "now", "name");
        mockAccountsList = new AccountsList(List.of(mockAccount));
        mockResponseEntity = ResponseEntity.ok(mockAccountsList);
        when(accountService.getAccountsList()).thenReturn(mockResponseEntity);

    }

    @Test
    void testGetDefaultAccount() {
        //Act
        Account defaultAccount = accountService.getDefaultAccount();

        // Assert
        assertNotNull(defaultAccount);
        assertEquals(mockAccount, defaultAccount);
    }

    @Test
    void testGetAccountsList_Success() {
        //Arrange
        when(urlBuilder.buildUrl("accounts")).thenReturn(mockUrl);
        when(headerConfig.constructHeader()).thenReturn(mockHeaders);
        when(restTemplate.exchange(eq(mockUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(AccountsList.class)))
                .thenReturn(mockResponseEntity);

        //Act
        ResponseEntity<AccountsList> response = accountService.getAccountsList();

        //Assert
        assertNotNull(response);
        assertEquals(mockAccountsList, response.getBody());
    }

    @Test
    void testGetAccountsList_Failure() {
        //Arrange and Act
        when(urlBuilder.buildUrl("accounts")).thenReturn(mockUrl);
        when(headerConfig.constructHeader()).thenReturn(mockHeaders);
        when(restTemplate.exchange(eq(mockUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(AccountsList.class)))
                .thenThrow(new RestClientException("Service unavailable"));

        //Assert
        assertThrows(RestClientException.class, () -> accountService.getAccountsList());
        verify(restTemplate, times(1)).exchange(eq(mockUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(AccountsList.class));
    }
}