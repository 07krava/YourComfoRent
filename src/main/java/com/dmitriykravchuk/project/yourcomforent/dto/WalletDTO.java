package com.dmitriykravchuk.project.yourcomforent.dto;

import com.dmitriykravchuk.project.yourcomforent.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDTO {
    private String currency;
    private BigDecimal balance;
    private BigDecimal frozenBalance;

    public static Wallet convertToWallet(WalletDTO walletDTO){
        Wallet wallet = new Wallet();
        wallet.setCurrency(walletDTO.getCurrency());
        wallet.setBalance(walletDTO.getBalance());
        wallet.setFrozenBalance(walletDTO.getFrozenBalance());
        return wallet;
    }

    public static WalletDTO convertToWalletDTO(Wallet wallet){
        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setCurrency(wallet.getCurrency());
        walletDTO.setBalance(wallet.getBalance());
        walletDTO.setFrozenBalance(wallet.getFrozenBalance());
        return walletDTO;
    }
}
