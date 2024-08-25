package com.starlingbank.roundUpSaving.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class UrlBuilderTest {
    private UrlBuilder urlBuilder;
    @BeforeEach
    public void setup() {
        urlBuilder = new UrlBuilder();
        ReflectionTestUtils.setField(urlBuilder, "baseUrl", "https://api.sandbox.starlingbank.com");
    }

    @Test
    public void testBuildUrl_withPathSegments() {
        String result = urlBuilder.buildUrl("account", "12345", "savings-goals");
        assertEquals("https://api.sandbox.starlingbank.com/api/v2/account/12345/savings-goals", result);
    }

    @Test
    public void testBuildUrl_withNoPathSegments() {
        String result = urlBuilder.buildUrl();
        assertEquals("https://api.sandbox.starlingbank.com/api/v2", result);
    }

    @Test
    public void testBuildParam() {
        String result = urlBuilder.buildParam("https://api.sandbox.starlingbank.com/api/v2/accounts", "changesSince", "someTime");
        assertEquals("https://api.sandbox.starlingbank.com/api/v2/accounts?changesSince=someTime", result);
    }

}