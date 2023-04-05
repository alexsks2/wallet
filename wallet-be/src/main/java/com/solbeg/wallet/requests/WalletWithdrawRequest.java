package com.solbeg.wallet.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class WalletWithdrawRequest{
    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal amount;
}
