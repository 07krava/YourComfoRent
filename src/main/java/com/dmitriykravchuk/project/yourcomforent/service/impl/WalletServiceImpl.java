package com.dmitriykravchuk.project.yourcomforent.service.impl;

import com.dmitriykravchuk.project.yourcomforent.model.User;
import com.dmitriykravchuk.project.yourcomforent.model.Wallet;
import com.dmitriykravchuk.project.yourcomforent.repository.UserRepository;
import com.dmitriykravchuk.project.yourcomforent.repository.WalletRepository;
import com.dmitriykravchuk.project.yourcomforent.service.WalletService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletServiceImpl(WalletRepository walletRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }
    @Override
    public Wallet createWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet updateWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public void deleteWallet(Long walletId) {
        walletRepository.deleteById(walletId);
    }

    @Override
    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId).orElse(null);
    }

    public void addMoneyToWallet(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

        Wallet wallet = user.getWallet();
        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal newBalance = currentBalance.add(amount);
        wallet.setBalance(newBalance);

        userRepository.save(user);
    }
}
