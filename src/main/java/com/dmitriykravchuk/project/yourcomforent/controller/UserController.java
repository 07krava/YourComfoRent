package com.dmitriykravchuk.project.yourcomforent.controller;

import com.dmitriykravchuk.project.yourcomforent.model.User;
import com.dmitriykravchuk.project.yourcomforent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addUser")
    public User createUser(@RequestBody User user){
        User userEntity = userService.createUser(user);
        return userEntity;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/allUsers")
    public List<User> allUsers(){
        return userService.listUsers();
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteUserById(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @PutMapping("/updateUser/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user){
        return userService.updateUser(id, user);
    }
}
