package com.bank.project.demo.requests;

import com.bank.project.demo.roles.CustomerRole;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

//Makes setters and getters for the given private variables within the class
@Getter
@Setter
public class RegistrationRequest
{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String validatePassword;
    private String gender = "Male";

    //Allows the data received from the html file to be seen as of LocalDate format
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private float deposit;
    private CustomerRole customerRole = CustomerRole.USER;

}
