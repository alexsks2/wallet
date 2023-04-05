package com.solbeg.wallet.mapper;

import com.solbeg.wallet.dto.WalletDto;
import com.solbeg.wallet.model.Wallet;
import com.solbeg.wallet.requests.CreateWalletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Mappings({
            @Mapping(source = "walletName", target = "name"),
            @Mapping(source = "currency", target = "currency"),
            @Mapping(target = "balance", constant = "0"),
            @Mapping(target = "id", ignore = true)
    })
    Wallet convert(CreateWalletRequest request);

    WalletDto convert(Wallet wallet);
}
