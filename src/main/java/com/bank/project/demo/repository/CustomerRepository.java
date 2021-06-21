package com.bank.project.demo.repository;

import com.bank.project.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    //Finds the Customer and checks if it exists
    @Query("SELECT s FROM Customer s WHERE s.email= ?1")
    Optional<Customer> findCustomerByEmail(String email);

    //Finds the Customer with the given email
    @Query("SELECT s FROM Customer s WHERE s.email= ?1")
    Customer findExactCustomerByEmail(String email);

    //The @Transactional, @Modifying and @Query allows the methods to be called during runtime and update the data of the user



    @Transactional
    @Modifying
    @Query("UPDATE Customer c " +
            "SET c.enabled = TRUE " +
            "WHERE c.email = ?1")
    void enableCustomer(String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Customer c " +
            "SET c.deposit = :deposit " +
            "WHERE c.email = :email")
    void updateDeposit(@Param("email") String email, @Param("deposit")float deposit);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Customer c " +
            "SET c.loan = :loan " +
            "WHERE c.email = :email")
    void addLoan(@Param("email") String email, @Param("loan")float loan);

}
