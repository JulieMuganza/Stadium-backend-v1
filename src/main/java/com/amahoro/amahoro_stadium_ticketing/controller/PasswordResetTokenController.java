//package com.amahoro.amahoro_stadium_ticketing.controller;
//
//
//import com.amahoro.amahoro_stadium_ticketing.model.PasswordResetToken;
//import com.amahoro.amahoro_stadium_ticketing.model.User;
//import com.amahoro.amahoro_stadium_ticketing.repository.UserRepository;
//import com.amahoro.amahoro_stadium_ticketing.service.PasswordResetService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Controller
//public class PasswordResetTokenController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordResetService passwordResetService;
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    // Display forgot password form
//    @GetMapping("/forgot-password")
//    public String showForgotPasswordForm() {
//        return "forgot-password";
//    }
//
//    // Handle password reset request
//    @PostMapping("/forgot-password")
//    public String processForgotPassword(@RequestParam("email") String email, Model model) {
//        Optional<User> user = userRepository.findByEmail(email);
//        if (user.isPresent()) {
//            PasswordResetToken resetToken = passwordResetService.createPasswordResetToken(user.get());
//
//            // Send email with reset link
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(user.get().getEmail());
//            message.setSubject("Password Reset Request");
//            message.setText("To reset your password, click the link below:\n" +
//                    "http://localhost:8080/reset-password?token=" + resetToken.getToken());
//            mailSender.send(message);
//
//            model.addAttribute("message", "Password reset link has been sent to your email.");
//        } else {
//            model.addAttribute("error", "Email address not found.");
//        }
//
//        return "forgot-password";
//    }
//
//    // Display reset password form
//    @GetMapping("/reset-password")
//    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
//        Optional<User> user = passwordResetService.getUserByPasswordResetToken(token);
//        if (user.isPresent()) {
//            model.addAttribute("token", token);
//            return "reset-password";
//        } else {
//            model.addAttribute("message", "Invalid or expired token.");
//            return "redirect:/forgot-password";
//        }
//    }
//
//    // Handle new password submission
//    @PostMapping("/reset-password")
//    public String processResetPassword(@RequestParam("token") String token,
//                                       @RequestParam("password") String password, Model model) {
//        Optional<User> user = passwordResetService.getUserByPasswordResetToken(token);
//        if (user.isPresent()) {
//            User resetUser = user.get();
//            resetUser.setPassword(new BCryptPasswordEncoder().encode(password)); // Save the encoded password
//            userRepository.save(resetUser);
//
//            passwordResetService.deletePasswordResetToken(token);
//            model.addAttribute("message", "Password reset successful. You can now log in.");
//            return "redirect:/login";
//        } else {
//            model.addAttribute("message", "Invalid or expired token.");
//            return "redirect:/forgot-password";
//        }
//    }
//
//    public void setToken(String token) {
//    }
//
//    public void setUser(User user) {
//    }
//
//    public void setExpiryDate(LocalDateTime localDateTime) {
//    }
//
//    public PasswordResetToken save(PasswordResetTokenController resetToken) {
//
//        return null;
//    }
//}