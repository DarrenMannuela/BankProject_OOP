package com.bank.project.demo.security.config;


import com.bank.project.demo.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration provides one or more @Bean methods and may be processed by the Spring container to generate bean
// definitions and service requests for those beans at runtime, whilst EnableSecurity allows the corresponding
// @Configuration to be applied globally
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    private final CustomerService customerService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //Configures the Spring boot security and provides which paths are allowed to be accessed by the user
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/RegisterNewCustomer/**").permitAll()
                .antMatchers("/ConfirmToken/**").permitAll()
                .antMatchers("/css/**","/scss/**","/vendor/**","/img/**","/js/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/login").defaultSuccessUrl("/User", true).permitAll()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login");
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    //@Bean annotation allows the method to be called by the Spring IoC container, which allows it to be instantiated instantly
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(customerService);
        return provider;
    }
}
