package com.solbeg.wallet.controller;

import com.solbeg.wallet.dto.WalletDto;
import com.solbeg.wallet.requests.CreateWalletRequest;
import com.solbeg.wallet.requests.WalletTransferRequest;
import com.solbeg.wallet.requests.WalletTopUpRequest;
import com.solbeg.wallet.requests.WalletWithdrawRequest;
import com.solbeg.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    @PostMapping()
    public WalletDto createWallet(@Valid @RequestBody CreateWalletRequest walletRequest) {
        return walletService.createWallet(walletRequest);
    }

    @GetMapping("/{id}")
    public WalletDto getWallet(@PathVariable UUID id) {
        return walletService.getWalletById(id);
    }

    @GetMapping()
    public List<WalletDto> getWalletsByUser() {
        return walletService.getWalletsByUser();
    }

    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable UUID id) {
        return walletService.getBalance(id);
    }

    @PatchMapping("/{id}/top-up")
    public WalletDto topUp(@PathVariable UUID id, @Valid @RequestBody WalletTopUpRequest topUpRequest) {
        return walletService.topUp(id, topUpRequest);
    }

    @PatchMapping("/{id}/withdraw")
    public WalletDto withdraw(@PathVariable UUID id, @Valid @RequestBody WalletWithdrawRequest withdrawRequest) {
        return walletService.withdraw(id, withdrawRequest);
    }

    @PatchMapping("/{id}/transfer")
    public String transferMoney(@PathVariable UUID id, @Valid @RequestBody WalletTransferRequest transferRequest) {
        walletService.transferMoney(id, transferRequest);
        return "OK";
    }


}
