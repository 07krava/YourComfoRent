package com.dmitriykravchuk.project.yourcomforent.controller;

import com.dmitriykravchuk.project.yourcomforent.dto.HousingDTO;
import com.dmitriykravchuk.project.yourcomforent.model.User;
import com.dmitriykravchuk.project.yourcomforent.service.HousingService;
import com.dmitriykravchuk.project.yourcomforent.service.UserService;
import com.dmitriykravchuk.project.yourcomforent.service.WalletService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final WalletService walletService;
    private final HousingService housingService;

    @Autowired
    public UserController(UserService userService, WalletService walletService, HousingService housingService) {
        this.walletService = walletService;
        this.userService = userService;
        this.housingService = housingService;
    }

    @PostMapping("/add")
    public ResponseEntity<HousingDTO> createHousing(@ModelAttribute HousingDTO housing,
                                                    @RequestParam("file") MultipartFile[] files,
                                                    @RequestParam("ownerId") Long ownerId) throws IOException, IOException {
        HousingDTO newHousing = housingService.createHousing(housing, files, ownerId);
        return new ResponseEntity<>(newHousing, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/allUsers")
    public List<User> allUsers() {
        return userService.listUsers();
    }

    @PutMapping("/updateUser/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @PostMapping("/addMoney/{userId}")
    public ResponseEntity<String> addMoneyToWallet(@PathVariable Long userId, @RequestParam BigDecimal amount){
        try{
            walletService.addMoneyToWallet(userId, amount);
            return ResponseEntity.ok("Money added successfully to the wallet.");
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body("User not found with id " + userId);
        }
    }

}
