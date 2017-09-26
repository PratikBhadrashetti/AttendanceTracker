/*package com.attendance.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

	@Resource
	Environment environment;

	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

	

	public void sendEmail(List<String> emailIds, String messageText) {

		String[] addressArray = new String[emailIds.size()];
		addressArray = emailIds.toArray(addressArray);

		InternetAddress[] inetAddresses = new InternetAddress[addressArray.length];

		for (int i = 0; i < addressArray.length; i++) {
			try {
				inetAddresses[i] = new InternetAddress(addressArray[i]);
			} catch (AddressException e) {
				e.printStackTrace();
			}
		}

		// for (String toMailId : emailIds) {
		logger.info("********************* Reminder ***********************");
		logger.info("Sending mail to the User: " + Arrays.toString(addressArray));
		String username = environment.getRequiredProperty("remaindermail.username");// "incedoincedo123@outlook.com";
																					// //
																					// like
																					// yourname@outlook.com
		String password = environment.getRequiredProperty("remaindermail.password");// "Mailincedo";
																					// //
																					// password
																					// here
		String host = environment.getRequiredProperty("remaindermail.host");
		String port = environment.getRequiredProperty("remaindermail.port");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		session.setDebug(false);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, inetAddresses); // like
																			// inzi769@gmail.com
			message.setSubject("Attendance Reminder");
			// message.setText("Hey Please submit attendance status for this
			// month.");

			if (messageText.isEmpty() || messageText == null) {
				message.setText("Dear Incedoer,\n" + "\n"
						+ "We hope that you have checked and updated your attendance. Please ensure all attendance of the current month.\n"
						+ "\n" + "Regards,\n" + "Attendance Team\n");
			} else {
				message.setText(
						"Dear Incedoer,\n" + "\n" + messageText + ".\n" + "\n" + "Regards,\n" + "Attendance Team\n");
			}
			Transport.send(message);
			logger.info("Reminder Sent to the MailId: " + Arrays.toString(addressArray));

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		// }

	}
}


package com.attendance.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EmailUtilOld {

	@Resource
	Environment environment;

	private static final Logger logger = LoggerFactory.getLogger(EmailUtilOld.class);

	

	public void sendEmail(List<String> emailIds, String messageText) {

		String[] addressArray = new String[emailIds.size()];
		addressArray = emailIds.toArray(addressArray);

		InternetAddress[] inetAddresses = new InternetAddress[addressArray.length];

		for (int i = 0; i < addressArray.length; i++) {
			try {
				inetAddresses[i] = new InternetAddress(addressArray[i]);
			} catch (AddressException e) {
				e.printStackTrace();
			}
		}

		// for (String toMailId : emailIds) {
		logger.info("********************* Reminder ***********************");
		logger.info("Sending mail to the User: " + Arrays.toString(addressArray));
		String username = environment.getRequiredProperty("remaindermail.username");// "incedoincedo123@outlook.com";
																					// //
																					// like
																					// yourname@outlook.com
		String password = environment.getRequiredProperty("remaindermail.password");// "Mailincedo";
																					// //
																					// password
																					// here
		String host = environment.getRequiredProperty("remaindermail.host");
		String port = environment.getRequiredProperty("remaindermail.port");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		session.setDebug(false);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, inetAddresses); // like
																			// inzi769@gmail.com
			message.setSubject("Attendance Reminder");
			// message.setText("Hey Please submit attendance status for this
			// month.");
			
			MimeBodyPart messageBodyPart = new MimeBodyPart();

	        Multipart multipart = new MimeMultipart();

	        messageBodyPart = new MimeBodyPart();
	        String file = "d:\\test.pdf";
	        String fileName = "test.pdf";
	        DataSource source = new FileDataSource(file);
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(fileName);
	        multipart.addBodyPart(messageBodyPart);

	        message.setContent(multipart);


			if (messageText.isEmpty() || messageText == null) {
				message.setText("Dear Incedoer,\n" + "\n"
						+ "We hope that you have checked and updated your attendance. Please ensure all attendance of the current month.\n"
						+ "\n" + "Regards,\n" + "Attendance Team\n");
			} else {
				message.setText(
						"Dear Incedoer,\n" + "\n" + messageText + ".\n" + "\n" + "Regards,\n" + "Attendance Team\n");
			}
			Transport.send(message);
			logger.info("Reminder Sent to the MailId: " + Arrays.toString(addressArray));

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		// }

	}
}*/