package com.amahoro.amahoro_stadium_ticketing.service;

import com.amahoro.amahoro_stadium_ticketing.model.PasswordResetToken;
import com.amahoro.amahoro_stadium_ticketing.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(User user);

    List<Object> isUserPresent(User user);


    User findUserByEmail(String email);


    String createPasswordResetTokenForUser(User user, String token);


    String createPasswordResetTokenForUser(String email);

    boolean validatePasswordResetToken(String token);


    void updatePassword(String token, String newPassword);

    Optional<PasswordResetToken> getPasswordResetToken(String token);
}