package com.amahoro.amahoro_stadium_ticketing.service;

import com.amahoro.amahoro_stadium_ticketing.model.PasswordResetToken;
import com.amahoro.amahoro_stadium_ticketing.model.User;
import com.amahoro.amahoro_stadium_ticketing.repository.PasswordResetTokenRepository;
import com.amahoro.amahoro_stadium_ticketing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Override
    public void saveUser(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public List<Object> isUserPresent(User user) {
        boolean userExists = false;
        String message = null;

        Optional<User> existingUserEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserEmail.isPresent()) {
            userExists = true;
            message = "Email Already Present!";
        }

        Optional<User> existingUserMobile = userRepository.findByMobile(user.getMobile());
        if (existingUserMobile.isPresent()) {
            userExists = true;
            message = "Mobile Number Already Present!";
        }

        if (existingUserEmail.isPresent() && existingUserMobile.isPresent()) {
            message = "Email and Mobile Number Both Already Present!";
        }

        return Arrays.asList(userExists, message);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public String createPasswordResetTokenForUser(User user, String token) {
        return null;
    }

    @Override
    public String createPasswordResetTokenForUser(String email) {
        return null;
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        return false;
    }

    @Override
    public void updatePassword(String token, String newPassword) {

    }

    @Override
    public Optional<PasswordResetToken> getPasswordResetToken(String token) {
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("USER_NOT_FOUND: " + email)
        );
    }
}