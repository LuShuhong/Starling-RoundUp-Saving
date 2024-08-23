package com.starlingbank.roundUpSaving.controller;

import com.starlingbank.roundUpSaving.model.RoundUpResponse;
import com.starlingbank.roundUpSaving.services.RoundUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/starling/v1")
public class RoundUpController {
    private final RoundUpService roundUpService;

    public RoundUpController(RoundUpService roundUpService) {
        this.roundUpService = roundUpService;
    }

    @GetMapping("/round-up")
    public ResponseEntity<RoundUpResponse> roundup() {
        RoundUpResponse roundUpResponse = roundUpService.executeRoundUp();
        return ResponseEntity.ok(roundUpResponse);
    }
}
