package com.starlingbank.roundUpSaving.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@Configuration
public class HeaderConfig {

    @Value("${customer.token.access}")
    private String accessToken;

    public HttpHeaders constructHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", accessToken);

        return httpHeaders;
    }
}
