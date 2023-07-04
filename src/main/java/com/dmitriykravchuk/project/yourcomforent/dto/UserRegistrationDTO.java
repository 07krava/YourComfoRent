package com.dmitriykravchuk.project.yourcomforent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private WalletDTO walletDTO;
}
