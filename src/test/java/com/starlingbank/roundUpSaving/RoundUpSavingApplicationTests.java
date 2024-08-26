package com.starlingbank.roundUpSaving;

import com.starlingbank.roundUpSaving.models.Amount;
import com.starlingbank.roundUpSaving.models.account.Account;
import com.starlingbank.roundUpSaving.models.savingsgoals.CreateSavingGoalResponse;
import com.starlingbank.roundUpSaving.models.transactions.FeedItem;
import com.starlingbank.roundUpSaving.models.transactions.FeedItemsList;
import com.starlingbank.roundUpSaving.services.AccountService;
import com.starlingbank.roundUpSaving.services.RoundUpService;
import com.starlingbank.roundUpSaving.services.TransactionService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest
class RoundUpSavingApplicationTests {
	private final Account mockAccount = new Account("I",
			"really",
			"love",
			"startling",
			"bank",
			"truly");
    private final Amount mockAmount1 = new Amount("GBP", 1234);
	private final Amount mockAmount2 = new Amount("GBP", 5678);
	private final Amount mockAmount3 = new Amount("GBP", 2222);
	private final FeedItemsList mockTransactions = new FeedItemsList(List.of(
			new FeedItem("1",mockAmount1, "OUT"),
			new FeedItem("2",mockAmount2, "OUT"),
			new FeedItem("3",mockAmount3, "OUT"),
			new FeedItem("4",mockAmount3, "IN")));

	@Mock
	private RestTemplate restTemplate;
	@InjectMocks
	private AccountService accountService;
	@InjectMocks
	private TransactionService mockTransactionService;
	@InjectMocks
	private RoundUpService mockRoundUpService;


	@Test
	void contextLoads() {
	}

	@Test
	void accountService_givenServerError_throwExceptions() {
		when(restTemplate.exchange(anyString(),
				eq(HttpMethod.PUT),
				any(HttpEntity.class),
				eq(CreateSavingGoalResponse.class)))
				.thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));
		assertThrows(Exception.class, () -> {
			accountService.getDefaultAccount();
		});
	}

	@Test
	void roundUpService_givenWrongAccount_throwExceptions() {
		assertThrows(Exception.class, () -> {
			mockRoundUpService.executeRoundUp(mockAccount);
		});
	}

	@Test
	void transactionService_givenTransactions_returnCorrectRoundUp() {
		assertEquals(166, mockTransactionService.getTotalRoundUpFromTransactions(mockTransactions));
	}
}
