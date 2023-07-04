package com.dmitriykravchuk.project.yourcomforent.service.impl;


import com.dmitriykravchuk.project.yourcomforent.dto.WalletDTO;
import com.dmitriykravchuk.project.yourcomforent.model.Role;
import com.dmitriykravchuk.project.yourcomforent.model.User;
import com.dmitriykravchuk.project.yourcomforent.model.Wallet;
import com.dmitriykravchuk.project.yourcomforent.repository.UserRepository;
import com.dmitriykravchuk.project.yourcomforent.repository.WalletRepository;
import com.dmitriykravchuk.project.yourcomforent.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private WalletRepository walletRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public User createUser(User user, WalletDTO walletDTO) {

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null){
            throw new RuntimeException("This user already exists!");
        }

        Wallet walletEntity = new Wallet();
        if (walletDTO != null) {
            walletEntity.setBalance(walletDTO.getBalance() != null ? walletDTO.getBalance() : BigDecimal.ZERO);
            walletEntity.setCurrency(walletDTO.getCurrency());
            walletEntity.setFrozenBalance(walletDTO.getFrozenBalance()!= null ? walletDTO.getFrozenBalance() : BigDecimal.ZERO);
        }
        walletEntity.setUser(user);

        user.setWallet(walletEntity);
        user.getRoles().add(Role.USER);
        walletRepository.save(walletEntity);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + id));
        return user;
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + id));
        userRepository.delete(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + id));
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhone(user.getPhone());
        userEntity.setPassword(user.getPassword());
        userEntity.setRoles(user.getRoles());
        userEntity.setBookings(user.getBookings());
        userEntity.setBookings(user.getBookings());

        return userRepository.save(userEntity);
    }
}

