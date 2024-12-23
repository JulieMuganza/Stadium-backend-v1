package com.amahoro.amahoro_stadium_ticketing.service;

import com.amahoro.amahoro_stadium_ticketing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailServiceTestRunner implements CommandLineRunner {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Override
    public void run(String... args) {
        // Specify the email address you want to test with
        String testEmail = "juliemuganza468@gmail.com";

        // Find the user by email (assuming the user already exists in your database)
        Optional<User> user = Optional.ofNullable(userService.findUserByEmail(testEmail));

        if (user.isPresent()) {
            // Generate a real token and save it
            String token = UUID.randomUUID().toString();
            passwordResetService.createPasswordResetToken(user.get(), token);

            // Construct the password reset link
            String resetLink = "http://localhost:8080/reset-password?token=" + token;

            // Send the password reset email
            emailService.sendPasswordResetEmail(testEmail, resetLink);

            System.out.println("Password reset email sent to " + testEmail + " with token: " + token);
        } else {
            System.out.println("User with email " + testEmail + " not found.");
        }
    }
}


