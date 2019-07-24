package com.demo.security.services;

import com.demo.model.ConfirmationToken;
import com.demo.model.User;
import com.demo.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailSenderService")
public class EmailSenderService {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public void sendEmail(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("khuongduy1484@gmail");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/api/auth/confirm-account?token=" + confirmationToken.getConfirmationToken());

        sendEmail(mailMessage);
    }
}