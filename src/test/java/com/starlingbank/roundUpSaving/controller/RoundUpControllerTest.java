package com.starlingbank.roundUpSaving.controller;

import com.starlingbank.roundUpSaving.models.account.Account;
import com.starlingbank.roundUpSaving.models.savingsgoals.SavingsGoalTransferResponse;
import com.starlingbank.roundUpSaving.services.AccountService;
import com.starlingbank.roundUpSaving.services.RoundUpService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoundUpController.class)
class RoundUpControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private RoundUpService roundUpService;

    @Test
    public void testSaveWeeklyRoundUp() throws Exception {
        //Arrange
        Account mockAccount = new Account("I", "really", "love", "startling", "bank", "truly");
        SavingsGoalTransferResponse mockResponse = new SavingsGoalTransferResponse("transferUid", true);

        when(accountService.getDefaultAccount()).thenReturn(mockAccount);
        when(roundUpService.executeRoundUp(mockAccount)).thenReturn(mockResponse);

        //Act and Assert
        mockMvc.perform(put("/starling/v1/save-my-weekly-round-up"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transferUid").value("transferUid"))
                .andExpect(jsonPath("$.success").value(true));
    }
}