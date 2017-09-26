/*package com.attendance.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.attendance.entity.User;
import com.attendance.repository.UserRepository;
import com.attendance.service.EmailService;

*//**
 * @author Ninganagouda.Patil
 *
 *//*

@Configuration
@EnableScheduling

public class MailScheduler {

	@Resource
	UserRepository userRepository;

	@Resource
	Environment environment;

	@Autowired
	EmailService mailService;

	private static final Logger logger = LoggerFactory.getLogger(MailScheduler.class);

	@Scheduled(initialDelay = 10000, fixedRate = 300000)
		//@Scheduled(initialDelay = 10000, fixedRate = 600000)
		//@Scheduled(cron="0 0/5 * 1/1 * ?") //Every Five minutes
		//@Scheduled(cron="0 0 12 1/1 * ? *")  // Every day @ 12 O clock 
		//@Scheduled(cron="0 0 12 ? * MON,TUE,WED,THU,FRI,SAT,SUN *") // Every Monday to Sunday 12 O Clock
		//@Scheduled(cron="0 0 0/12 1/1 * ? *") // Every Hour
		//@Scheduled(cron="0 0 12 1 1/1 ? *") // First Day Of Every Month @ 12 O Clock
	public void run() {
		logger.info(
				"*************************************************************************************************************");
		logger.info("MailScheduler started @ the time " + new Date());
		try {
			int countOfManagers = Integer.parseInt(environment.getRequiredProperty("count_of_managers"));
			mailService.insertManagersAndClients(countOfManagers);
			mailService.CheckSubmitStatusForCurrMonth();

			List<User> managerList = (List<User>) userRepository.findAll();

			String saveDirectory = environment.getRequiredProperty("saveDirectory");
			String protocol = environment.getRequiredProperty("incedomail.protocol");
			
			String port = environment.getRequiredProperty("incedomail.port");
			String url = environment.getRequiredProperty("incedomail.url");

			Properties props = new Properties();
			props.setProperty("mail.store.protocol", protocol);

			// extra codes required for reading OUTLOOK mails during IMAP-start
			props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.imaps.socketFactory.fallback", "false");
			props.setProperty("mail.imaps.port", port);
			props.setProperty("mail.imaps.socketFactory.port", port);
			props.setProperty("-Dmail.imaps.auth.plain.disable", "true");
			
			for(User userFromDB:managerList)
			{
				logger.info("Manager Email ----> "+userFromDB.getUsermail());
				logger.info("Manager Name  ----> "+userFromDB.getUsername());
				String username = userFromDB.getUsermail();// environment.getRequiredProperty("incedomail.username");
				String password = userFromDB.getPassword(); // environment.getRequiredProperty("incedomail.password");

				// extra codes required for reading OUTLOOK mails during IMAP-end
				try {
					Session session = Session.getInstance(props);
					Store store = session.getStore(protocol);
					store.connect(url, username, password);
					Folder folderInbox = store.getFolder("INBOX");
					folderInbox.open(Folder.READ_ONLY);

					// fetches new messages from server
					Message[] arrayMessages = folderInbox.getMessages();

					logger.info("Total Number Of Messages From MailInbox " + arrayMessages.length);

					for (int i = 0; i < arrayMessages.length; i++) {
						Message message = arrayMessages[i];
						Address[] fromAddress = message.getFrom();
						String fromFromMail = fromAddress[0].toString();
						String splitArrayForMail[] = fromFromMail.split("<");
						String from = splitArrayForMail[1].replace(">", "");
						String subject = message.getSubject();
						String sentDate = message.getSentDate().toString();

						String contentType = message.getContentType();
						String messageContent = "";

						// store attachment file name, separated by comma
						String attachFiles = "";

						if (contentType.contains("multipart")) {
							// content may contain attachments
							Multipart multiPart = (Multipart) message.getContent();
							int numberOfParts = multiPart.getCount();
							for (int partCount = 0; partCount < numberOfParts; partCount++) {
								MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
								if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
									// this part is attachment
									String fileName = part.getFileName();
									attachFiles += fileName + ", ";
									part.saveFile(saveDirectory + File.separator + fileName);
								} else {
									// this part may be the message content
									messageContent = part.getContent().toString();
								}
							}

							if (attachFiles.length() > 1) {
								attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
							}
						} else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
							Object content = message.getContent();
							if (content != null) {
								messageContent = content.toString();
							}
						}

						// print out details of each message
						logger.info("Message #" + (i + 1) + ":");
						logger.info("\t From: " + from);
						logger.info("\t Subject: " + subject);
						logger.info("\t Sent Date: " + sentDate);
						logger.debug("\t Message: " + messageContent);
						logger.info("\t Attachments: " + attachFiles);

						String splitArray[] = messageContent.split("<p>");
						logger.debug(Arrays.asList(splitArray) + " Split Arry List ");
						String datesFromMailBody = splitArray[1].replaceAll("</p>", "");
						logger.info("\t Dates in MailBody: " + Arrays.asList(datesFromMailBody));

						String splitArrayFromSubject[] = subject.split("_");
						String datesArrayFromMailBody[] = datesFromMailBody.split(",");

						// Ninganagouda_602513_Affirmed_July_2017

						String empName = splitArrayFromSubject[0];
						String empId = splitArrayFromSubject[1];
						int a = Integer.parseInt(empId);
						String clientName = splitArrayFromSubject[2];
						String monthFromMailSubject = splitArrayFromSubject[3];
						int yearFromMailSubject = Integer.parseInt(splitArrayFromSubject[4]);
						;
						String status = mailService.checkSubmitStatus(a, monthFromMailSubject, yearFromMailSubject + "");
						if (status.equals("NoRecord")) {
							logger.info("If NoRecord Then Insert");
							mailService.insertData(datesArrayFromMailBody, splitArrayFromSubject, from,
									monthFromMailSubject, yearFromMailSubject, userFromDB.getId() + "");
						}
						if(status.equals("NotSubmitted"))
						{
							logger.info("Changing status from not_submitted to Submitted");
							mailService.insertDataWhenNotSubmit(datesArrayFromMailBody, splitArrayFromSubject, from,
									monthFromMailSubject, yearFromMailSubject, userFromDB.getId() + "");
						}

					}

					// disconnect
					folderInbox.close(false);
					store.close();
				} catch (NoSuchProviderException ex) {
					logger.info("No provider for pop3.");
					ex.printStackTrace();
				} catch (MessagingException ex) {
					logger.info("Could not connect to the message store");
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
			}
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.info("MailScheduler ended @ the time " + new Date());
			logger.info(
					"*************************************************************************************************************");

		}

	}

}
*/