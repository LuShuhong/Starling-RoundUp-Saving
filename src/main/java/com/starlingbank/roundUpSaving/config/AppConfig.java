package com.starlingbank.roundUpSaving.config;

import com.starlingbank.roundUpSaving.model.Account;
import com.starlingbank.roundUpSaving.services.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Account account(AccountService accountService) {
        return Objects.requireNonNull(accountService.getAccountsList().getBody())
                .accounts()
                .get(0);
    }
}
