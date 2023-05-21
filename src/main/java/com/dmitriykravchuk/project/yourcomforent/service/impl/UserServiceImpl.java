package com.dmitriykravchuk.project.yourcomforent.service.impl;


import com.dmitriykravchuk.project.yourcomforent.model.User;
import com.dmitriykravchuk.project.yourcomforent.repository.UserRepository;
import com.dmitriykravchuk.project.yourcomforent.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null){
            throw new RuntimeException("This user already exists!");
        }
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
        userEntity.setBookings(user.getBookings());

        return userRepository.save(userEntity);
    }
}

