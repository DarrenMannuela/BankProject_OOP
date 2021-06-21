package com.bank.project.demo.controller;
import com.bank.project.demo.entity.Customer;
import com.bank.project.demo.repository.CustomerRepository;
import com.bank.project.demo.requests.TakeLoanRequest;
import com.bank.project.demo.requests.UpdateDepositRequest;
import com.bank.project.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

//An annotation that allows the class to act as a request handler
@Controller
public class CustomerController
{
    // Declaring private variables of the CustomerController class
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private String userEmail;


    //Allows the dependencies from the configurations file to be directly injected into the instances of the class
    @Autowired
    public CustomerController(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;

    }


    //When the URL path to /User is provided the function loads the User.html, the ModelMap parameter is used to implement the data of the user
    // can be accessed through the html file
    @GetMapping(path = "/User")
    public String customerPage(ModelMap model)
    {
        // The Authentication class is used to get access of the current username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userEmail = auth.getName();
        Customer customer = customerRepository.findExactCustomerByEmail(userEmail);
        model.addAttribute("customer", customer);
        return "/User";
    }


    //When the URL path to /UpdateDeposit is provided the function, loads the UpdateDeposit.html
    @GetMapping(path = "/UpdateDeposit")
    public String redirectToAddDeposit(ModelMap model)
    {
        //Allows the user to fill in a request for updating their deposit
        model.addAttribute("updateDepositRequest", new UpdateDepositRequest());
        return "/UpdateDeposit";
    }


    // Handles the POST request from the /UpdateDeposit URL path
    @PostMapping(path = "/UpdateDeposit")
    public String redirectUpdateDepositToUser(@ModelAttribute("updateDepositRequest")
                                                          UpdateDepositRequest updateDepositRequest)

    {
        //Finds the user's email and password within the service layer
        customerService.validateAndUpdateDeposit(userEmail, updateDepositRequest.getChoice(),
                updateDepositRequest.getUpdateDeposit(), updateDepositRequest.getPassword());

        return "redirect:/User";
    }

    //When the URL path to /TakeLoan is provided the function, loads the TakeLoan.html
    @GetMapping(path = "TakeLoan")
    public String redirectToLoan(ModelMap model)
    {
        //Allows the user to fill in a request for taking a loan
        model.addAttribute("takeLoanRequest", new TakeLoanRequest());
        return "/TakeLoan";
    }

    // Handles the POST request from the /TakeLoan URL path
    @PostMapping(path = "/TakeLoan")
    public String redirectLoanToUser(@ModelAttribute("takeLoanRequest") TakeLoanRequest takeLoanRequest)
    {
        customerService.takeLoanRequest(userEmail, takeLoanRequest.getLoan(), takeLoanRequest.getPassword());
        return "redirect:/User";
    }


}
