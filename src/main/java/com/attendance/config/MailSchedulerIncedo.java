package com.attendance.config;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.attendance.entity.DateOfMonth;
import com.attendance.entity.User;
import com.attendance.repository.UserRepository;
import com.attendance.service.EmailService;
import com.attendance.utils.DateUtils;

/**
 * @author Ninganagouda.Patil
 *
 */

@Configuration
@EnableScheduling

public class MailSchedulerIncedo {

	@Resource
	UserRepository userRepository;

	@Resource
	Environment environment;

	@Autowired
	EmailService mailService;
	
	@Autowired
	DateUtils dateUtils;

	private static final Logger logger = LoggerFactory.getLogger(MailSchedulerIncedo.class);

		//@Scheduled(initialDelay = 10000, fixedRate = 300000)
		//@Scheduled(initialDelay = 10000, fixedRate = 600000)
		@Scheduled(cron="0 0/5 * 1/1 * ?") //Every Five minutes
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
			
			String subjetForMailBody = environment.getRequiredProperty("subjetForMailBody");
			String saveDirectory = environment.getRequiredProperty("saveDirectory");
			String protocol = environment.getRequiredProperty("incedomail.protocol");
			
			String port = environment.getRequiredProperty("incedomail.port");
			String url = environment.getRequiredProperty("incedomail.url");

			
			String host = "mail.incedoinc.com";
			Properties properties = System.getProperties();
			for(User userFromDB:managerList)
			{
				logger.info("Manager Email ----> "+userFromDB.getUsermail());
				logger.info("Manager Name  ----> "+userFromDB.getUsername());
				String username = userFromDB.getUsermail();// environment.getRequiredProperty("incedomail.username");
				String password = userFromDB.getPassword(); // environment.getRequiredProperty("incedomail.password");

				// extra codes required for reading OUTLOOK mails during IMAP-end
				try {
					Session session = Session.getDefaultInstance(properties);
				    Store store = session.getStore("pop3");
				    store.connect(host, username, password);
					Folder folderInbox = store.getFolder("INBOX");
					folderInbox.open(Folder.READ_ONLY);
					Date startDate = dateUtils.MonthBeginingDate();
					Date endDate = dateUtils.MonthEndingDate();
					SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, startDate);
					SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.LT, endDate);
					
					SearchTerm andTerm = new AndTerm(olderThan, newerThan);
					// fetches Messages only for current month
					// Message[] arrayMessages = folderInbox.search(andTerm);
					Flags seen = new Flags(Flags.Flag.SEEN);
					Message[] arrayMessages = folderInbox.search(new FlagTerm(seen, false));
					logger.info("Total Number Of Messages From MailInbox " + arrayMessages.length);
					
					for (int i = arrayMessages.length-1; i>0; i--) {
						Message message = arrayMessages[i];
						// logger.info("Comparision Mail "+message.getSentDate()+" "+startDate+" "+i+" "+message.getSentDate().compareTo(startDate)+" "+message.getSentDate().after(startDate)+" "+message.getSentDate().before(endDate));
						if (message.getSentDate().after(startDate) && message.getSentDate().before(endDate))  // message.getSentDate().after(startDate) &&
					       {
							Address[] fromAddress = message.getFrom();
							String fromFromMail = fromAddress[0].toString();
							String subject = message.getSubject();
							String sentDate = message.getSentDate().toString();
							StringBuffer subjectToCheck = new StringBuffer();
							subjectToCheck.append(subject);
							String attachFiles = "";
							List<String> list = new ArrayList<>();
							ArrayList<String> dateArrayList = new ArrayList<>();
							try
							{
								
								if(subjectToCheck.substring(0, 20).equalsIgnoreCase(subjetForMailBody))
								{
									String contentType = message.getContentType();
									String messageContent = "";
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
												
												// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
												  FileInputStream inputStream = new FileInputStream(new File(saveDirectory + File.separator + fileName));
									                
									                Workbook workbook = new XSSFWorkbook(inputStream);
									                Sheet firstSheet = workbook.getSheetAt(0);
									                Iterator<Row> iterator = firstSheet.iterator();
									                int count = 0;
									                
									                while (iterator.hasNext() && count<=2) {
									                	StringBuffer s = new StringBuffer();
									                	s.append("");
									                    Row nextRow = iterator.next();
									                    Iterator<Cell> cellIterator = nextRow.cellIterator();
									                    count = count+1;
									                    while (cellIterator.hasNext()) {
									                    	
									                        Cell cell = cellIterator.next();
									                        
									                        if(cell.getCellType()==0)
									                        {
									                        	if(count==2)
									                        	{
										                        	s = s.append(cell.getNumericCellValue()+"$");
										                        	dateArrayList.add(cell.getNumericCellValue()+"");
										                        	
									                        	}
									                        	else
									                        	{
										                        	s = s.append(cell.getDateCellValue()+"$");
										                        	dateArrayList.add(cell.getDateCellValue()+"");
										                        	
									                        	}
									                        	
									                        }
									                        else
									                        {
									                        	 switch (cell.getCellType()) {
										                            case Cell.CELL_TYPE_STRING:
										                                s = s.append(cell.getStringCellValue()+"$");
										                                break;
										                            case Cell.CELL_TYPE_BOOLEAN:
										                                s = s.append(cell.getBooleanCellValue()+"$");
										                                break;
										                            case Cell.CELL_TYPE_NUMERIC:
										                                s = s.append(cell.getNumericCellValue()+"$");
										                                break;
										                        
										                        }
									                        }
									                        
									                       
									                    }
									                    list.add(s.toString()); 
									                }
									                 
									                workbook.close();
									                inputStream.close();
									             
											} else {
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
									// store attachment file name, separated by comma
									
									
									
									String split1 = subject.replace("Attendance details :", "");
									String[] splitArrayFromSubject = split1.split("_");
									String monthFromMailSubject = splitArrayFromSubject[3];
									String yearFromMailSubject = splitArrayFromSubject[4];
									int empId = Integer.parseInt(splitArrayFromSubject[1]);;
									
									String status = mailService.checkSubmitStatus(empId, monthFromMailSubject, yearFromMailSubject);
									if (status.equals("NoRecord")) {
										// print out details of each message
										logger.info("Message #" + (i + 1) + ":");
										logger.info("\t From: " + fromFromMail);
										logger.info("\t Subject: " + subject);
										logger.info("\t Sent Date: " + sentDate);
										//logger.debug("\t Message: " + messageContent);
										logger.info("\t Attachments: " + attachFiles);
										logger.info("\t Headers : " + list.get(0));
										logger.info("\t First Row Values : " + list.get(1));
										
										logger.info("If NoRecord Then Insert");
										mailService.insertExcelData(list.get(0).toString().trim(),list.get(1).toString().trim(),dateArrayList,subject,userFromDB.getId()+"",fromFromMail);
									}
									if(status.equals("NotSubmitted"))
									{
										// print out details of each message
										logger.info("Message #" + (i + 1) + ":");
										logger.info("\t From: " + fromFromMail);
										logger.info("\t Subject: " + subject);
										logger.info("\t Sent Date: " + sentDate);
										//logger.debug("\t Message: " + messageContent);
										logger.info("\t Attachments: " + attachFiles);
										logger.info("\t Headers : " + list.get(0));
										logger.info("\t First Row Values : " + list.get(1));
										
										 String headerArry[] = list.get(0).toString().split("\\$");
										 String valueArry[] = list.get(1).toString().split("\\$");
										 HashMap<String, String> map = new HashMap<>();
										 for(int k=0;k<headerArry.length;k++)
										 {
											 map.put(headerArry[k].toString().trim(), valueArry[k].toString().trim());
										 }
										DateOfMonth dateObjectForDbFromMail = mailService.GetMonthData(map, dateArrayList);
										logger.info("Changing status from not_submitted to Submitted");
										mailService.insertDataWhenNotSubmit(dateObjectForDbFromMail, splitArrayFromSubject, fromFromMail,
												monthFromMailSubject, yearFromMailSubject, userFromDB.getId() + "");
									}
									
									/*String status = mailService.checkSubmitStatus(a, monthFromMailSubject, yearFromMailSubject + "");
									*/
									
									
								}
								else
								{
									
								}
							}
							catch(Exception e)
							{
								logger.error("Exception "+e.getMessage());
								// e.printStackTrace();
							}
					       }
						
					}

					// disconnect
					folderInbox.close(false);
					store.close();
				} catch (NoSuchProviderException ex) {
					logger.error("No provider for pop3. and Error Message "+ex.getMessage());
				} catch (MessagingException ex) {
					logger.error("Could not connect to the message store"+ex.getMessage());
					
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
