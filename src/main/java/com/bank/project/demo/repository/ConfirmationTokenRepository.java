package com.bank.project.demo.repository;

import com.bank.project.demo.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

//A specified annotation of the @Component annotation that allows the capability to encapsulating storage, retrieval,
// and search behaviour which emulates a collection of objects
@Repository

//The interface extends to JpaRepository to access the full API of CrudRepository and PagingAndSortingRepository.
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long>
{
    //Method to find token
    Optional<ConfirmationToken> findByToken(String token);

    //The @Transactional, @Modifying and @Query allows the methods to be called during runtime and update the data of the token entity
    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")

    //Updates the data of the ConfirmedAt of a token entity within the database
    void updateConfirmedAt(String token, LocalDateTime now);
}
