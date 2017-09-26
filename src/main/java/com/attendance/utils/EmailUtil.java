package com.attendance.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.BodyPart; 
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
public class EmailUtil {

	@Resource
	Environment environment;

	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);
	
	

	public void sendEmail(List<String> emailIds, String messageText,String subject) {
		
		String subjetForMailBody = environment.getRequiredProperty("subjetForMailBody");
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
	         
	         message.setRecipients(Message.RecipientType.TO,inetAddresses);
	        	 //	 InternetAddress.parse("ninganagouda.patil@incedoinc.com"));
	            // InternetAddress.parse("incedoakshay@outlook.com"));
	         
	         message.setSubject(subjetForMailBody+subject);
	         
	         BodyPart messageBodyPart = new MimeBodyPart();
	         if(messageText==null)
	         {
	        	 messageBodyPart.setText("Please fill attendance details and send attached file as same subject");
	         }
	         else
	         {
	        	 messageBodyPart.setText(messageText);
	         }
	         
	         
	         Multipart multipart = new MimeMultipart();
	         
	         multipart.addBodyPart(messageBodyPart);
	         
	         messageBodyPart = new MimeBodyPart();
	         try {
	        	 int monthCount = new Date().getMonth()+1;
	        	 System.out.println("File Name "+environment.getRequiredProperty("mailAttachmentDir")+environment.getRequiredProperty("attachmentName")+monthCount+".xlsx");
				copyFileUsingStream(new File(environment.getRequiredProperty("mailAttachmentDir")+environment.getRequiredProperty("attachmentName")+monthCount+".xlsx"), new File(environment.getRequiredProperty("mailAttachmentDir")+subject+".xlsx"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         String filename = environment.getRequiredProperty("mailAttachmentDir")+subject+".xlsx";
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);
	         // Send the complete message parts
	         message.setContent(multipart);
	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }

	}
	
	
	private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            try
            {
            	is.close();
                os.close();
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            }
        	
        }
    }
}