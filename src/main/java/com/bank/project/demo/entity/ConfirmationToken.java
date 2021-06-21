package com.bank.project.demo.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    //Auto increments the id of the ConfirmationToken class
    @Id
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )

    private Long id;

    //Column is made un-nullable
    @Column(nullable = false)
    private String token;

    //Column to keep track when the token is made
    @Column(nullable = false)
    private LocalDateTime createdAt;

    //Column to keep track when the token expires
    @Column(nullable = false)
    private LocalDateTime expireAt;

    //Column to keep track when the token is confirmed
    private LocalDateTime confirmedAt;

    //The annotation will not put rhe variable into the database and the variable is used to check if the the token
    // is correct
    @Transient
    private String validateToken;

    //Connects the token to a Customer
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "customer_id"
    )
    private Customer customer;


    //Constructor for the ConfirmationToken
    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiredAt, Customer customer)
    {
        this.token = token;
        this.createdAt = createdAt;
        this.expireAt = expiredAt;
        this.customer = customer;
    }


}
