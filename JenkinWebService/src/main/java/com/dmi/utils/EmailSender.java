package com.dmi.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dmi.constant.Constants;
import com.dmi.security.JWTTokenService;


/**
 * @author Ajay Negi
 */

@Service
public class EmailSender
{
	@Autowired
	private JavaMailSender mailSender;

	public void sendEmailConfirmationMail(String jwtSubject, String username, String email)
			throws MessagingException, FileNotFoundException
	{
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String confirmationLink = Constants.APPLICATION_PROJECT_URL + "/email/confirmEmail?jwt=Bearer "
				+ JWTTokenService.issueEmailConfirmationToken(jwtSubject, username, email);

		String mailText = "";
		ClassLoader classLoader = getClass().getClassLoader();
		Scanner scanner = new Scanner(new File(classLoader.getResource("registrationEmailTemplate.html").getFile()));
		while (scanner.hasNextLine())
			mailText += scanner.nextLine();
		scanner.close();

		mailText = mailText.replaceAll("username", username);
		mailText = mailText.replaceAll("%s", confirmationLink);

		mimeMessage.setContent(mailText, "text/html");
		helper.setTo(email);
		helper.setSubject(Constants.REGISTRATION_EMAIL_TOPIC);
		helper.setFrom(Constants.FROM_EMAIL_ADDRESS);
		mailSender.send(mimeMessage);
	}

	
	public void sendVHROnDemandMail(String emailContent, String email, String month, String year)
			throws MessagingException, FileNotFoundException
	{
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		
		String mailText = emailContent;
		
		mimeMessage.setContent(mailText, "text/html");
		helper.setTo(email);
		helper.setSubject(Constants.JWT_SUBJECT_VHR_ON_DEMAND + " " + month.toUpperCase() + " " + year);
		helper.setFrom(Constants.FROM_EMAIL_ADDRESS);
		mailSender.send(mimeMessage);
	}
	public void sendPasswordResetMail(String jwtSubject, String username, String email) throws MessagingException
	{
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String confirmationLink = Constants.UI_PROJECT_URL + "/resetPassword?q="
				+ JWTTokenService.issueEmailConfirmationToken(jwtSubject, username, email);
		String mailText = String.format(
				"Dear %s,<br/> Click <a href='%s'>HERE</a> to reset your Password for IoT User Portal OR copy paste below URL in your browser's address bar. <br/><br/> <a href='%s'>%s</a>",
				username, confirmationLink, confirmationLink, confirmationLink);

		mimeMessage.setContent(mailText, "text/html");
		helper.setTo(email);
		helper.setSubject(Constants.PASSWORD_RESET_EMAIL_TOPIC);
		helper.setFrom(Constants.FROM_EMAIL_ADDRESS);
		mailSender.send(mimeMessage);
	}

}
