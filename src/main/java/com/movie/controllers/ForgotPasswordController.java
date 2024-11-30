package com.movie.controllers;

import com.movie.auth.entities.ForgotPassword;
import com.movie.auth.entities.User;
import com.movie.auth.repositories.ForgotPasswordRepository;
import com.movie.auth.repositories.UserRepository;
import com.movie.auth.utils.ChangePassword;
import com.movie.dto.MailBody;
import com.movie.service.Email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    private final UserRepository  userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    //send  mail for email verification
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("Please provide a valid email"));
        int otp=otpGenerator();
        MailBody  mailBody=MailBody.builder()
                .to(email)
                .subject("OTP for Forgot Password request")
                .text("This is the OTP for your Forgot Password Request:"+otp)
                .build();

        ForgotPassword fp=ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis()+70*1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email sent for verification !");
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp,@PathVariable String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(fp.getFpid());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP has expired");
        }

        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@PathVariable String email,
                                                        @RequestBody ChangePassword changePassword){
        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match! Please try again");
        }

        String encodedPassword=passwordEncoder.encode(changePassword.password());
        userRepository.updateUserPassword(email,encodedPassword);
        return ResponseEntity.ok("Password has been changed");
    }

    private Integer otpGenerator(){
        Random random=new Random();
        return random.nextInt(100_000,999_999);
    }
}
