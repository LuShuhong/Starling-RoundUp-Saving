package com.starlingbank.roundUpSaving.configs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;

class HeaderConfigTest {
    private HeaderConfig headerConfig;
    @BeforeEach
    public void setup() {
        headerConfig = new HeaderConfig();
        ReflectionTestUtils.setField(headerConfig, "accessToken", "Bearer mockAccessToken");
    }

    @Test
    public void testConstructHeader() {
        HttpHeaders headers = headerConfig.constructHeader();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals("Bearer mockAccessToken", headers.getFirst("Authorization"));
        assertEquals(MediaType.APPLICATION_JSON, headers.getAccept().get(0));
    }
}