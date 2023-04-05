package com.solbeg.wallet.service;

import com.solbeg.wallet.dto.UserDto;
import com.solbeg.wallet.exception.WalletException;
import com.solbeg.wallet.dto.WalletDto;
import com.solbeg.wallet.model.Wallet;
import com.solbeg.wallet.requests.CreateWalletRequest;
import com.solbeg.wallet.requests.WalletTransferRequest;
import com.solbeg.wallet.requests.WalletTopUpRequest;
import com.solbeg.wallet.requests.WalletWithdrawRequest;
import com.solbeg.wallet.repository.WalletRepository;
import com.solbeg.wallet.mapper.WalletMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletDto createWallet(CreateWalletRequest walletRequest) {

        walletRequest.setUserId(getCurrentUserId());
        Wallet wallet = WalletMapper.INSTANCE.convert(walletRequest);
        Wallet savedWallet = walletRepository.save(wallet);

        return WalletMapper.INSTANCE.convert(savedWallet);
    }

    public WalletDto getWalletById(UUID walletId) {

        return WalletMapper.INSTANCE
                .convert(walletRepository.findByIdAndUserId(walletId, getCurrentUserId())
                        .orElseThrow(() -> new WalletException("Wallet not found")));
    }

    public List<WalletDto> getWalletsByUser() {

        return walletRepository.findAllByUserId(getCurrentUserId()).stream()
                .map(WalletMapper.INSTANCE::convert)
                .toList();
    }

    public BigDecimal getBalance(UUID walletId) {

        return walletRepository.getBalanceByIdAndUserId(walletId, getCurrentUserId());
    }

    public WalletDto topUp(UUID walletId, WalletTopUpRequest walletTopUpRequest) {

        Wallet wallet = walletRepository.findByIdAndUserId(walletId, getCurrentUserId())
                .orElseThrow(() -> new WalletException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(walletTopUpRequest.getAmount()));

        return WalletMapper.INSTANCE.convert(walletRepository.save(wallet));
    }

    public WalletDto withdraw(UUID walletId, WalletWithdrawRequest walletWithdrawRequest) {

        Wallet wallet = walletRepository.findByIdAndUserId(walletId, getCurrentUserId())
                .orElseThrow(() -> new WalletException("Wallet not found"));
        BigDecimal balance = wallet.getBalance().subtract(walletWithdrawRequest.getAmount());

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletException("Insufficient funds");
        }

        wallet.setBalance(balance);

        return WalletMapper.INSTANCE.convert(walletRepository.save(wallet));
    }

    public void transferMoney(UUID walletId, WalletTransferRequest transferRequest) {

        Wallet source = walletRepository.findByIdAndUserId(walletId, getCurrentUserId())
                .orElseThrow(() -> new WalletException("Wallet not found"));

        BigDecimal sourceBalance = source.getBalance().subtract(transferRequest.getAmount());

        if (sourceBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletException("Insufficient funds");
        }

        source.setBalance(sourceBalance);

        Wallet destination = walletRepository.findByIdAndUserId(transferRequest.getWalletId(), getCurrentUserId())
                .orElseThrow(() -> new WalletException("Wallet not found"));

        destination.setBalance(destination.getBalance().add(transferRequest.getAmount()));

        walletRepository.saveAll(Arrays.asList(source, destination));
    }

    private UUID getCurrentUserId() {
        return ((UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

}
