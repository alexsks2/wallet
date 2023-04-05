package com.solbeg.wallet.service;

import com.solbeg.wallet.dto.UserDto;
import com.solbeg.wallet.dto.WalletDto;
import com.solbeg.wallet.exception.WalletException;
import com.solbeg.wallet.model.Wallet;
import com.solbeg.wallet.requests.CreateWalletRequest;
import com.solbeg.wallet.requests.WalletTransferRequest;
import com.solbeg.wallet.requests.WalletTopUpRequest;
import com.solbeg.wallet.requests.WalletWithdrawRequest;
import com.solbeg.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc()
public class WalletServiceTest {

    private WalletRepository walletRepository;

    private WalletService walletService;

    private static final UUID USER_ID = UUID.randomUUID();


    @BeforeEach
    public void setup() {

        walletRepository = Mockito.mock(WalletRepository.class);
        walletService = new WalletService(walletRepository);
        UserDto userDto = new UserDto();
        userDto.setId(USER_ID);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDto, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Test
    void testGetWalletById() {

        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setName("Test Wallet");
        wallet.setBalance(BigDecimal.valueOf(5));
        wallet.setCurrency("EUR");
        when(walletRepository.findByIdAndUserId(walletId, USER_ID)).thenReturn(Optional.of(wallet));

        WalletDto walletDto = walletService.getWalletById(walletId);

        assertEquals(walletId, walletDto.getId());
        assertEquals("Test Wallet", walletDto.getName());
        assertEquals(BigDecimal.valueOf(5), walletDto.getBalance());
        assertEquals("EUR", walletDto.getCurrency());
    }

    @Test
    void testGetWalletsByUser() {

        Wallet wallet1 = new Wallet();
        wallet1.setId(UUID.randomUUID());
        wallet1.setName("TestWallet1");
        wallet1.setBalance(BigDecimal.valueOf(100));
        wallet1.setCurrency("EUR");
        Wallet wallet2 = new Wallet();
        wallet2.setId(UUID.randomUUID());
        wallet2.setName("TestWallet2");
        wallet2.setBalance(BigDecimal.valueOf(200));
        wallet2.setCurrency("EUR");
        List<Wallet> wallets = Arrays.asList(wallet1, wallet2);
        when(walletRepository.findAllByUserId(USER_ID)).thenReturn(wallets);

        List<WalletDto> listOfWallets = walletService.getWalletsByUser();

        assertEquals(2, listOfWallets.size());
        assertEquals(wallet1.getId(), listOfWallets.get(0).getId());
        assertEquals(wallet1.getName(), listOfWallets.get(0).getName());
        assertEquals(wallet1.getBalance(), listOfWallets.get(0).getBalance());
        assertEquals(wallet1.getCurrency(), listOfWallets.get(0).getCurrency());
        assertEquals(wallet2.getId(), listOfWallets.get(1).getId());
        assertEquals(wallet2.getName(), listOfWallets.get(1).getName());
        assertEquals(wallet2.getBalance(), listOfWallets.get(1).getBalance());
        assertEquals(wallet2.getCurrency(), listOfWallets.get(1).getCurrency());

    }

    @Test
    void testCreateWallet() {

        CreateWalletRequest request = new CreateWalletRequest();
        request.setWalletName("Test Wallet");
        request.setCurrency("EUR");
        when(walletRepository.save(Mockito.any(Wallet.class))).thenAnswer(i -> i.getArguments()[0]);

        WalletDto walletDto = walletService.createWallet(request);

        assertEquals("Test Wallet", walletDto.getName());
        assertEquals(BigDecimal.ZERO, walletDto.getBalance());
        assertEquals("EUR", walletDto.getCurrency());
    }

    @Test
    void testTopUp() {

        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(123.45);
        WalletTopUpRequest walletTopUpRequest = new WalletTopUpRequest();
        walletTopUpRequest.setAmount(amount);

        Wallet wallet = new Wallet(walletId, "Test wallet", BigDecimal.ZERO, "EUR", UUID.randomUUID());
        when(walletRepository.findByIdAndUserId(walletId, USER_ID)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);

        WalletDto walletDto = walletService.topUp(walletId, walletTopUpRequest);

        Mockito.verify(walletRepository).findByIdAndUserId(walletId, USER_ID);
        Mockito.verify(walletRepository).save(wallet);

        assertEquals(amount, wallet.getBalance());
        assertEquals(wallet.getId(), walletDto.getId());
        assertEquals(wallet.getBalance(), walletDto.getBalance());
        assertEquals(wallet.getName(), walletDto.getName());
        assertEquals(wallet.getCurrency(), walletDto.getCurrency());
    }

    @Test
    void testWithdraw() {

        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(10.0);
        WalletWithdrawRequest walletWithdrawRequest = new WalletWithdrawRequest();
        walletWithdrawRequest.setAmount(amount);

        Wallet wallet = new Wallet(walletId, "Test wallet", BigDecimal.TEN, "EUR", UUID.randomUUID());
        when(walletRepository.findByIdAndUserId(walletId, USER_ID)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);

        WalletDto walletDto = walletService.withdraw(walletId, walletWithdrawRequest);

        Mockito.verify(walletRepository).findByIdAndUserId(walletId, USER_ID);
        Mockito.verify(walletRepository).save(wallet);

        BigDecimal expectedBalance = BigDecimal.valueOf(0.0);
        assertEquals(expectedBalance, walletDto.getBalance());
        assertEquals(wallet.getId(), walletDto.getId());
        assertEquals(wallet.getBalance(), walletDto.getBalance());
        assertEquals(wallet.getName(), walletDto.getName());
        assertEquals(wallet.getCurrency(), walletDto.getCurrency());
    }

    @Test
    void testWithdraw_NegativeCase() {

        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(15);
        WalletWithdrawRequest walletWithdrawRequest = new WalletWithdrawRequest();
        walletWithdrawRequest.setAmount(amount);

        Wallet wallet = new Wallet(walletId, "Test wallet", BigDecimal.TEN, "EUR", UUID.randomUUID());
        when(walletRepository.findByIdAndUserId(walletId, USER_ID)).thenReturn(Optional.of(wallet));

        assertThrows(WalletException.class, () -> walletService.withdraw(walletId, walletWithdrawRequest));
    }

    @Test
    void testTransferMoney() {

        UUID sourceWalletId = UUID.randomUUID();
        UUID destinationWalletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.TEN;

        Wallet sourceWallet = new Wallet(sourceWalletId, "Source Wallet", BigDecimal.TEN, "EUR", UUID.randomUUID());
        Wallet destWallet = new Wallet(destinationWalletId, "Dest wallet", BigDecimal.ZERO, "EUR", UUID.randomUUID());

        when(walletRepository.findByIdAndUserId(sourceWalletId, USER_ID)).thenReturn(Optional.of(sourceWallet));
        when(walletRepository.findByIdAndUserId(destinationWalletId, USER_ID)).thenReturn(Optional.of(destWallet));
        when(walletRepository.saveAll(Arrays.asList(sourceWallet, destWallet))).thenReturn(Arrays.asList(sourceWallet, destWallet));

        walletService.transferMoney(sourceWalletId, new WalletTransferRequest(destinationWalletId, amount));

        assertEquals(BigDecimal.ZERO, sourceWallet.getBalance());
        assertEquals(BigDecimal.TEN, destWallet.getBalance());
        Mockito.verify(walletRepository).saveAll(Arrays.asList(sourceWallet, destWallet));
    }

}