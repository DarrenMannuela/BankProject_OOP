package com.bank.project.demo.entity;

import com.bank.project.demo.roles.CustomerRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Customer implements UserDetails {

    //Used to auto increment the value of Id after a customer object is instantiated
    @Id
    @SequenceGenerator(
            name = "customer_sequence",
            sequenceName = "customer_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_sequence"
    )

    private long id;

    //Specify first name and last name of customer
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Transient
    @Enumerated(EnumType.STRING)
    private CustomerRole customerRole=CustomerRole.USER;

    //gender will be specified
    private String gender;

    private LocalDate dob;

    private float deposit;
    private float loan;

    @Transient
    private int age;



    //Access checkers
    private Boolean locked ;
    private Boolean enabled = false;


    //Constructor for the Customer class, since teh Id of the Customer is auto incremented within the database it is not
    // required to be added within the constructor
    public Customer(String firstName, String lastName, String email ,String password, String gender,
                    LocalDate dob, float deposit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.deposit = deposit;
        this.loan = 0;
        this.locked = true;
        this.enabled = false;
    }

    //The function to automatically get the age of a person based on their given Birthdate and
    public int getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }


    //Overrides the methods of the interface class UserDetails
    //Returns the authorities the user is granted
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(customerRole.name());
        return Collections.singletonList(authority);
    }

    //Returns the password of the authenticated user
    @Override
    public String getPassword() {
        return password;
    }

    //Returns the username of the authenticated user
    @Override
    public String getUsername() {
        return email;
    }

    //Returns true if the account is not expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //If true the account is not locked anf the user is able to access their account
    @Override
    public boolean isAccountNonLocked() {
        return locked;

    }

    //Returns true as the password does not expire
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Returns false as the the user must verify their account
    @Override
    public boolean isEnabled() {
        return enabled;
    }





    @Override
    public String toString() {
        return "customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", deposit=" + deposit +
                ", loan=" + loan +
                '}';
    }



}
