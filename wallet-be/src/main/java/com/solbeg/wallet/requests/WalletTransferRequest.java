package com.solbeg.wallet.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransferRequest {

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal amount;

    @NotNull
    private UUID walletId;

    public WalletTransferRequest(UUID walletId, @NotNull @DecimalMin(value = "0.00") BigDecimal amount) {

        this.amount = amount;
        this.walletId = walletId;
    }
}
