package com.solbeg.wallet.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletRequest {

    @NotBlank
    private String walletName;

    @NotBlank
    private String currency;

    private UUID userId;
}
