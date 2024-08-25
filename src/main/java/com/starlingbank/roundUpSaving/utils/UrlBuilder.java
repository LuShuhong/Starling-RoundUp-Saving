package com.starlingbank.roundUpSaving.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UrlBuilder {
    @Value("${starling.sandbox.host}")
    private String baseUrl;

    public String buildUrl(String... pathSegments) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment("api", "v2");
        for (String segment : pathSegments) {
            builder.pathSegment(segment);
        }
        return builder.toUriString();
    }

    public String buildParam(String endpoint, String paramName, String paramValue) {
        return UriComponentsBuilder.fromUriString(endpoint)
                .queryParam(paramName, paramValue)
                .toUriString();
    }
}
