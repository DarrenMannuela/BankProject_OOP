package com.bank.project.demo.service;

import com.bank.project.demo.entity.Customer;
import com.bank.project.demo.requests.RegistrationRequest;
import com.bank.project.demo.entity.ConfirmationToken;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationService
{

    private final CustomerService customerService;
    private final ConfirmationTokenService confirmationTokenService;

    //Register's a new user into the database
    public void register(RegistrationRequest request) throws MessagingException {
        customerService.addNewCustomer(
                new Customer(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(),
                        request.getGender(), request.getDob(), request.getDeposit()));
    }

    //Checks to see if the email has the corresponding token
    @Transactional
    public void confirmToken(String token) throws MessagingException {
        ConfirmationToken confirmationToken = confirmationTokenService.
                getToken(token).orElseThrow(()-> new IllegalStateException("Token does not exist"));

        //If the token is confirmed
        if(confirmationToken.getConfirmedAt()!=null){
            throw new IllegalStateException("Email has been confirmed at " + confirmationToken.getConfirmedAt());
        }

        //Gets the expired at of te token
        LocalDateTime expiredDate = confirmationToken.getExpireAt();

        //Checks if the token has expired
        if(expiredDate.isBefore(LocalDateTime.now())){
            Customer currCustomer = confirmationToken.getCustomer();
            String newToken = UUID.randomUUID().toString();
            ConfirmationToken renewToken  = new ConfirmationToken(newToken,
                    LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), currCustomer);
            confirmationTokenService.saveToken(renewToken);
            customerService.sendEmail(currCustomer, newToken);
            throw new IllegalStateException("Token expired");
        }

        //Set the confirmed at the given token and enable the user's account
        confirmationTokenService.setConfirmedAt(token);
        customerService.enableCustomerAccount(
                confirmationToken.getCustomer().getEmail());

    }
}
