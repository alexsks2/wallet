package com.solbeg.wallet.repository;

import com.solbeg.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    Collection<Wallet> findAllByUserId(UUID userId);

    Optional<Wallet> findByIdAndUserId(UUID walletId, UUID userId);

    BigDecimal getBalanceByIdAndUserId(UUID walletId, UUID userId);
}
