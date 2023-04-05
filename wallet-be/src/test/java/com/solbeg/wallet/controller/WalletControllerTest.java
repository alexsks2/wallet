package com.solbeg.wallet.controller;

import com.solbeg.wallet.dto.WalletDto;
import com.solbeg.wallet.requests.CreateWalletRequest;
import com.solbeg.wallet.requests.WalletTransferRequest;
import com.solbeg.wallet.requests.WalletTopUpRequest;
import com.solbeg.wallet.requests.WalletWithdrawRequest;
import com.solbeg.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletControllerTest extends AbstractMvcTest {

    @MockBean
    private WalletService walletService;

    @Test
    void testCreateWallet_shouldReturnValidResponse() throws Exception {

        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(100);
        String name = "Test Wallet";
        String currency = "EUR";
        WalletDto wallet = new WalletDto(walletId, name, balance, currency);
        CreateWalletRequest walletRequest = new CreateWalletRequest(name, currency, UUID.randomUUID());
        given(walletService.createWallet(any())).willReturn(wallet);

        mockMvc.perform(post("/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(walletRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.balance").value(100))
                .andExpect(jsonPath("$.currency").value(currency));

        verify(walletService, times(1)).createWallet(walletRequest);
    }

    @Test
    void testGetWallet_shouldReturnValidResponse() throws Exception {

        UUID walletId = UUID.randomUUID();
        WalletDto wallet = new WalletDto(walletId, "Test wallet", BigDecimal.TEN, "EUR");
        given(walletService.getWalletById(walletId)).willReturn(wallet);

        mockMvc.perform(get("/wallet/{id}", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(walletId.toString())))
                .andExpect(jsonPath("$.balance", is(10)))
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.name", is("Test wallet")));

        verify(walletService, times(1)).getWalletById(walletId);
    }

    @Test
    void testGetWalletsByUser_shouldReturnValidResponse() throws Exception {

        WalletDto wallet1 = new WalletDto(UUID.randomUUID(), "Test wallet 1", new BigDecimal("10.0"), "EUR");
        WalletDto wallet2 = new WalletDto(UUID.randomUUID(), "Test wallet 2", new BigDecimal("0.0"), "EUR");
        List<WalletDto> wallets = Arrays.asList(wallet1, wallet2);
        given(walletService.getWalletsByUser()).willReturn(wallets);

        mockMvc.perform(get("/wallet")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(wallet1.getId().toString()))
                .andExpect(jsonPath("$.[0].balance").value(wallet1.getBalance()))
                .andExpect(jsonPath("$.[0].currency").value(wallet1.getCurrency()))
                .andExpect(jsonPath("$.[0].name").value(wallet1.getName()))
                .andExpect(jsonPath("$.[1].id").value(wallet2.getId().toString()))
                .andExpect(jsonPath("$.[1].balance").value(wallet2.getBalance()))
                .andExpect(jsonPath("$.[0].currency").value(wallet1.getCurrency()))
                .andExpect(jsonPath("$.[0].name").value(wallet1.getName()));

        verify(walletService, times(1)).getWalletsByUser();
    }

    @Test
    void testGetBalance_shouldReturnValidResponse() throws Exception {

        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(100);
        given(walletService.getBalance(walletId)).willReturn(balance);

        mockMvc.perform(get("/wallet/" + walletId + "/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(balance));
    }

    @Test
    void testTopUp_shouldReturnValidResponse() throws Exception {

        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(50);
        WalletTopUpRequest walletTopUpRequest = new WalletTopUpRequest();
        walletTopUpRequest.setAmount(amount);
        WalletDto wallet = new WalletDto(walletId, "Test Wallet", BigDecimal.valueOf(100), "EUR");

        given(walletService.topUp(walletId, walletTopUpRequest)).willReturn(wallet);

        mockMvc.perform(patch("/wallet/" + walletId + "/top-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(walletTopUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.name").value(wallet.getName()))
                .andExpect(jsonPath("$.balance").value(100))
                .andExpect(jsonPath("$.currency").value(wallet.getCurrency()));
    }


    @Test
    void testWithdraw_shouldReturnValidResponse() throws Exception {

        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(50);
        WalletWithdrawRequest walletWithdrawRequest = new WalletWithdrawRequest();
        walletWithdrawRequest.setAmount(amount);
        WalletDto wallet = new WalletDto(walletId, "Test Wallet", BigDecimal.valueOf(100), "EUR");

        given(walletService.withdraw(walletId, walletWithdrawRequest)).willReturn(wallet);

        mockMvc.perform(patch("/wallet/" + walletId + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(walletWithdrawRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.name").value(wallet.getName()))
                .andExpect(jsonPath("$.balance").value(100))
                .andExpect(jsonPath("$.currency").value(wallet.getCurrency()));
    }

    @Test
    void testTransferMoney_shouldReturnAcceptedStatus() throws Exception {

        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(50);
        UUID recipientWalletId = UUID.randomUUID();
        WalletTransferRequest walletTransferRequest = new WalletTransferRequest();
        walletTransferRequest.setWalletId(recipientWalletId);
        walletTransferRequest.setAmount(amount);


        mockMvc.perform(patch("/wallet/" + walletId + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(walletTransferRequest)))
                .andExpect(status().isOk());
        verify(walletService, times(1)).transferMoney(walletId, walletTransferRequest);
    }
}