package com.bank.project.demo.controller;



import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//An annotation that allows the class to act as a request handler
@Controller
public class LoginController
{

    //Gets the URL path for /login, loads the login.html
    @GetMapping(path="/login")
    public String loginPage()
    {
        //If User tries to go back to the login page from the /User path this will restrict it
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth==null||auth instanceof AnonymousAuthenticationToken)
        {
            return"/login";
        }
        return "redirect:/User";
    }


}

