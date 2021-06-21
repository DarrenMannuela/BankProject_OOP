package com.bank.project.demo.service;

import com.bank.project.demo.entity.Customer;
import com.bank.project.demo.entity.ConfirmationToken;
import com.bank.project.demo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService implements UserDetailsService {

    private final static String customerNotFound = " The customer with the email %s was not found";
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final JavaMailSender mailSender;


    //Finds the customer within the repository and if not throw an error
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerRepository.findCustomerByEmail(email).orElseThrow(()->
                new UsernameNotFoundException(String.format(customerNotFound, email)));
    }

    public void sendEmail(Customer customer, String token) throws MessagingException {
        // Sends the confirmation token via email
        String senderEmail = "darren.mannuela@gmail.com";
        String receiverEmail = "darren.mannuela@gmail.com";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(senderEmail);
        helper.setTo(receiverEmail);
        helper.setSubject("Bank confirmation ");
        helper.setText(String.format("Dear %s %s,\nThe confirmation is: %s", customer.getFirstName(),
                customer.getLastName(), token) , true);

        mailSender.send(message);
    }

    //Adds a new user into the database
    public void addNewCustomer(Customer newCustomer) throws MessagingException {
        //Checks if the email is already within the database
        boolean customerExist = customerRepository.findCustomerByEmail(newCustomer.getEmail()).isPresent();

        if(customerExist)
        {
            throw new IllegalStateException("Email has been taken");
        }

        //Encodes the password of the new user
        String encodedPassword = bCryptPasswordEncoder.encode(newCustomer.getPassword());
        newCustomer.setPassword(encodedPassword);

        //Saves the user into the database
        customerRepository.save(newCustomer);

        //Makes a confirmation token for the new user
        String newToken = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken  = new ConfirmationToken(newToken,
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), newCustomer);

        //Save the token into the database
        confirmationTokenService.saveToken(confirmationToken);

        sendEmail(newCustomer, newToken);

    }


    //Updates and validates the user's deposit
    public void validateAndUpdateDeposit(String email, String choice, float deposit, String password)
    {

        Customer customer = customerRepository.findExactCustomerByEmail(email);

        // checks if the encrypted password matches the password given by the user
        if(bCryptPasswordEncoder.matches(password, customer.getPassword()))
        {

            //if the user's choice parameter was deposit
            if(choice.equals("deposit"))
            {
                float newDeposit = customer.getDeposit()+deposit;
                customerRepository.updateDeposit(email, newDeposit);
            }
            //if the user's choice parameter was takeout
            else if(choice.equals("takeout"))
            {
                //Checks if the user has the amount that wants to be taken out
                if(customer.getDeposit()>=deposit)
                {
                    float newDeposit = customer.getDeposit()-deposit;
                    customerRepository.updateDeposit(email, newDeposit);
                }

            }

        }

    }

    //If the user would like to take a loan
    public void takeLoanRequest(String email, float loan, String password)
    {
        Customer customer = customerRepository.findExactCustomerByEmail(email);
        float newLoan = customer.getLoan()+loan;

        // checks if the encrypted password matches the password given by the user
        if(bCryptPasswordEncoder.matches(password, customer.getPassword()))
        {
            customerRepository.addLoan(email, newLoan);
        }
    }


    //Enables the user's account if the confirmation token was correct
    public void enableCustomerAccount(String email){
        customerRepository.enableCustomer(email);
    }

}
