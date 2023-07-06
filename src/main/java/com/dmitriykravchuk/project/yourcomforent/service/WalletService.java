package com.dmitriykravchuk.project.yourcomforent.service;

import com.dmitriykravchuk.project.yourcomforent.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    Wallet createWallet(Wallet wallet);

    Wallet updateWallet(Wallet wallet);

    void deleteWallet(Long walletId);

    Wallet getWalletById(Long walletId);

    void addMoneyToWallet(Long userId, BigDecimal amount);
}