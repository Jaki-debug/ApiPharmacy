package com.pharmacie.pharmacie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service  
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;  

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);  // 
    }

	public void sendTestEmail(String to) {
		// TODO Auto-generated method stub
		
	}
	@Service
	public class MailService {

	    @Autowired
	    private JavaMailSender mailSender;

	    public void sendTestEmail() {
	        try {
	            SimpleMailMessage message = new SimpleMailMessage();
	            message.setFrom("noreply@sikaytech.com"); 
	            message.setTo("lineguidi@gmail.com"); 
	            message.setSubject("Test Mailtrap");
	            message.setText("Ceci est un test d'envoi d'e-mail avec Mailtrap et Spring Boot.");

	            mailSender.send(message);
	            System.out.println("E-mail envoyé avec succès !");
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Erreur lors de l'envoi de l'e-mail.");
	        }
	    }
	}

	
}
