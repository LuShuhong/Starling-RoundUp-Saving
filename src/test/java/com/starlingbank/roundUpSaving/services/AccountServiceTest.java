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

    private Account mockAccount;
    private AccountsList mockAccountsList;
    private ResponseEntity<AccountsList> mockResponseEntity;
    private String mockUrl;
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
        mockAccount =  new Account("I", "really", "love", "startling", "bank", "truly");
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
    void testGetDefaultAccount_NullBody(){
        //Arrange and Act
        when(urlBuilder.buildUrl("accounts")).thenReturn(mockUrl);
        when(headerConfig.constructHeader()).thenReturn(mockHeaders);
        ResponseEntity<AccountsList> mockResponseEntity = ResponseEntity.ok(null);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(AccountsList.class)))
                .thenReturn(mockResponseEntity);

        // Act
        assertThrows(NullPointerException.class, () -> accountService.getDefaultAccount());
    }

    @Test
    void testGetAccountsList() {
        //Arrange
        when(urlBuilder.buildUrl("accounts")).thenReturn(mockUrl);
        when(headerConfig.constructHeader()).thenReturn(mockHeaders);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(AccountsList.class)))
                .thenReturn(mockResponseEntity);

        //Act
        ResponseEntity<AccountsList> response = accountService.getAccountsList();

        //Assert
        assertNotNull(response);
        assertEquals(mockAccountsList, response.getBody());
    }
}