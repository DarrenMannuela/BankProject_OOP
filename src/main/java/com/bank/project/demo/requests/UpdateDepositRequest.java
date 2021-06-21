package com.bank.project.demo.requests;


import lombok.Getter;
import lombok.Setter;

//Makes setters and getters for the given private variables within the class
@Getter
@Setter
public class UpdateDepositRequest
{
    private String choice = "deposit";
    private float updateDeposit;
    private String password;

}
