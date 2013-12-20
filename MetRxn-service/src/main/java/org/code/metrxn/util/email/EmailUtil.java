package org.code.metrxn.util.email;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import org.code.metrxn.util.Logger;

/**
 * 
 * @author ambika_b
 *
 */
public class EmailUtil {

	public static boolean sendEmail(String userName, String subject, String contents) {
		final String from = "metrxn@gmail.com";
		final String pass = "metrxn123";
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props, 
				new javax.mail.Authenticator() { 
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, pass); 
					} 
				});
		try{
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(userName));
			message.setSubject(subject);
			message.setContent(contents, "text/html" );
			Transport.send(message);
			System.out.println("Message sent successfully :D");
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
		return false;
	}
	
	public static void main(String[] args) {
		Logger.info("Sending mail...", EmailUtil.class);
		sendEmail("aub282@psu.edu","MetRxn user account activation", "<h2>Please click on the url to activate your account!!</h2>"
		+ "<p><h1><a href= 'http://localhost:8080/MetRxn-service/services/user/activate/'>Activate account.</a></p></h1>");
		Logger.info("mail sent!!", EmailUtil.class);
	}
}