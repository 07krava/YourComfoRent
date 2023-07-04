package com.dmitriykravchuk.project.yourcomforent.repository;

import com.dmitriykravchuk.project.yourcomforent.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}

