package com.bank.project.demo.test;

import com.bank.project.demo.entity.Customer;
import com.bank.project.demo.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;

//These are test accounts
@Configuration
public class CustomerTest {



    @Bean
    CommandLineRunner commandLineRunner(CustomerService customerService)
    {
        return args -> {
            Customer darren = new Customer("Darren",
                    "Mannuela","darren.mannuela@gmail.com", "123", "Male",
                    LocalDate.of(2003, Month.APRIL, 12), 1000);

            Customer jamie = new Customer("Jamie",
                    "Lex","jamie.lex" +
                    "@gmail.com","321", "Female",
                    LocalDate.of(1999, Month.JANUARY, 1), 1000);


            customerService.addNewCustomer(darren);
            customerService.addNewCustomer(jamie);

            customerService.enableCustomerAccount("jamie.lex@gmail.com");
            customerService.enableCustomerAccount("darren.mannuela@gmail.com");
        };
    }
}
