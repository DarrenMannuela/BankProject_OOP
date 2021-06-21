package com.bank.project.demo.controller;


import com.bank.project.demo.entity.ConfirmationToken;
import com.bank.project.demo.requests.RegistrationRequest;
import com.bank.project.demo.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;

//An annotation that allows the class to act as a request handler
@Controller

//An annotation to create a constructor with all private variables
@AllArgsConstructor

public class RegistrationController
{

    private final RegistrationService registrationService;

    //When the URL path to /RegisterNewCustomer is provided the function and loads RegisterNewCustomer.html
    @GetMapping(path="/RegisterNewCustomer")
    public String registerNewCustomer(ModelMap model)
    {
        //Allows the user to fill in a request to register
        model.addAttribute("registerRequest", new RegistrationRequest());
        return "/RegisterNewCustomer";
    }

    //When the URL path to /RegisterNewCustomer is provided the function and loads RegisterNewCustomer.html
    @GetMapping(path="/ConfirmToken")
    public String confirmNewCustomer(@ModelAttribute("registerRequest") RegistrationRequest registrationRequest,
                                     ModelMap model) throws MessagingException
    {
        //The user is requested for the confirmation token
        model.addAttribute("token", new ConfirmationToken());

        //Register's the new user
        registrationService.register(registrationRequest);
        return "/ConfirmToken";
    }

    //Handles the POST method of the /ConfirmToken URL path
    @PostMapping(path="/ConfirmToken")
    public String confirmNewCustomerSuccess(@ModelAttribute("token") ConfirmationToken token) throws MessagingException {
        //Checks if the token is valid
        registrationService.confirmToken(token.getValidateToken());


        return "/login";
    }

}



