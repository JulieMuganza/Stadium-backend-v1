package com.amahoro.amahoro_stadium_ticketing.controller;

import com.amahoro.amahoro_stadium_ticketing.model.User;
import com.amahoro.amahoro_stadium_ticketing.service.EmailService;
import com.amahoro.amahoro_stadium_ticketing.service.PasswordResetService;
import com.amahoro.amahoro_stadium_ticketing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    @GetMapping("/forgot-password")
//    public String showForgotPasswordForm() {
//        return "passwordreset/forgotpassword";
//    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = Optional.ofNullable(userService.findUserByEmail(email));
        if (userOpt.isPresent()) {
            String token = UUID.randomUUID().toString();
            passwordResetService.createPasswordResetToken(userOpt.get(), token);

            // Ensure correct link format and values
            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(email, resetLink);

            redirectAttributes.addFlashAttribute("message", "Password reset link sent to your email.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Email not found.");
        }
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Optional<User> resetToken = passwordResetService.getUserByPasswordResetToken(token);
        if (resetToken.isPresent()) {
            model.addAttribute("token", token);
            return "passwordreset/resetpassword";
        } else {
            model.addAttribute("error", "Invalid or expired token.");
            return "redirect:/forgot-password";
        }
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = passwordResetService.getUserByPasswordResetToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userService.saveUser(user);

            passwordResetService.deletePasswordResetToken(token);
            redirectAttributes.addFlashAttribute("message", "Password reset successfully.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired token.");
            return "redirect:/forgot-password";
        }
    }

//    @CrossOrigin(origins = {}, allowCredentials = "false")
//    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
//    public String login() {
//
//        return "auth/login";
//    }

//    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
//    public String register(Model model) {
//        model.addAttribute("user", new User());
//        return "auth/register";
//    }

//    @CrossOrigin(origins = {}, allowCredentials = "false")
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        System.out.println("Register");
        List<Object> userPresentObj = userService.isUserPresent(user);
        if (userPresentObj != null && !userPresentObj.isEmpty() && (Boolean) userPresentObj.get(0)) {

            return "Failed to register";
        }

        userService.saveUser(user);


        return "Successfully Data";
    }

//    @CrossOrigin(origins = "*", allowCredentials = "false")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        if(user.getEmail() == null && user.getPassword() == null){
            return ResponseEntity.status(200).body("Enter Email or password");
        }

        User userEmail = userService.findUserByEmail(user.getEmail());
        Boolean decodedPassword = Boolean.valueOf(String.valueOf(bCryptPasswordEncoder.matches(user.getPassword(),userEmail.getPassword())));

        if(decodedPassword){
            System.out.println("Role"+userEmail.getRole());
            return ResponseEntity.ok(userEmail.getRole());
        }else{
            System.out.println("userPassword"+userEmail.getPassword());
            System.out.println("password"+user.getPassword());
            System.out.println("DecodedPassword"+decodedPassword);
            return ResponseEntity.status(404).body("Not Logged in");
        }
    }
}