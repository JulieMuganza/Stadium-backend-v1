package com.amahoro.amahoro_stadium_ticketing.service;

import com.amahoro.amahoro_stadium_ticketing.model.PasswordResetToken;
import com.amahoro.amahoro_stadium_ticketing.model.User;
import com.amahoro.amahoro_stadium_ticketing.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public PasswordResetToken createPasswordResetToken(User user, String token) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        return tokenRepository.save(resetToken);
    }

    public Optional<User> getUserByPasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken != null && resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            return Optional.of(resetToken.getUser());
        }
        return Optional.empty();
    }

    public void deletePasswordResetToken(String token) {
        tokenRepository.deleteByToken(token);
    }
}