package com.dmitriykravchuk.project.yourcomforent.service;

import com.dmitriykravchuk.project.yourcomforent.model.Wallet;

public interface WalletService {

    Wallet createWallet(Wallet wallet);

    Wallet updateWallet(Wallet wallet);

    void deleteWallet(Long walletId);

    Wallet getWalletById(Long walletId);
}