package com.bank.project.demo.service;


import com.bank.project.demo.entity.ConfirmationToken;
import com.bank.project.demo.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Getter
@Setter
public class ConfirmationTokenService
{
    private final ConfirmationTokenRepository confirmationTokenRepository;
    public void saveToken(ConfirmationToken token)
    {
        confirmationTokenRepository.save(token);
    }

    //Gets the token within the database
    public Optional<ConfirmationToken> getToken(String token)
    {
        return confirmationTokenRepository.findByToken(token);
    }

    //Gets the time the token is confirmed at
    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }


}
