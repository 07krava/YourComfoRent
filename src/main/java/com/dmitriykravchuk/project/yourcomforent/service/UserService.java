package com.dmitriykravchuk.project.yourcomforent.service;

import com.dmitriykravchuk.project.yourcomforent.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    List<User> listUsers();

    void deleteUser(Long id);

    User updateUser(Long id, User user);
}
