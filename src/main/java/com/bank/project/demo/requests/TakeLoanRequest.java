package com.bank.project.demo.requests;

import lombok.*;

//Makes setters and getters for the given private variables within the class
@Getter
@Setter
public class TakeLoanRequest
{
    private float loan;
    private String password;
}
