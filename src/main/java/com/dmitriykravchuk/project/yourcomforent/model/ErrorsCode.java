package com.dmitriykravchuk.project.yourcomforent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorsCode {
    INSUFFICIENT_FUNDS("Not enough money in your account. Please top up your account."),
    MAXIMUM_GUESTS_EXCEEDED("Maximum number of occupants exceeded."),
    HOUSING_ALREADY_BOOKED("The housing is already booked for the dates indicated."),
    DEFAULT_ERROR_CODE("Неизвестная ошибка. Пожалуйста, обратитесь в службу поддержки.");

    private String description;

    public String getDescription() {
        return description;
    }
}
