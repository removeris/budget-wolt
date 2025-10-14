package com.example.budgetwolt.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Driver extends BasicUser {
    private String driverLicense;
    private LocalDate dateOfBirth;

    public Driver(String username, String password, String name, String surname, String phoneNumber, String address, String driverLicense, LocalDate dateOfBirth) {
        super(username, password, name, surname, phoneNumber, address);
        this.driverLicense = driverLicense;
        this.dateOfBirth = dateOfBirth;
    }
}
