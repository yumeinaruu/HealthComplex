package com.project.healthcomplex.service;

import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Data
@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String fromEmail;

    private String[] cc = {"stas.lisavoy@icloud.com"};
    private final String registrationBody = "You have successfully registered your account!";
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendEmailNoAttachment(String to, String[] cc, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setCc(cc);
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(mimeMessage);
            return "mail sent successfully";
        } catch (Exception e) {
            System.out.println(e);
        }
        return "mail sent failed";
    }
}
