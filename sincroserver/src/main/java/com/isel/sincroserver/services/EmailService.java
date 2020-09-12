package com.isel.sincroserver.services;

import com.isel.sincroserver.entities.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Qualifier("EmailService")
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(Citizen citizen, String verificationCode) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(citizen.getEmail());

        msg.setSubject("Verifique a sua identidade");
        msg.setText("Insira na aplicação o código \"" + verificationCode + "\" para completar o registo.");

        javaMailSender.send(msg);
    }
}
