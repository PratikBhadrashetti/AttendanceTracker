package com.attendance.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.exception.AttendanceException;
import com.attendance.model.EmailRequest;
import com.attendance.model.EmailResponse;
import com.attendance.repository.ClientRepository;
import com.attendance.service.EmailService;

/**
 * @author harsha.patil
 *
 */

@RestController
@RequestMapping(value = "/email")
public class EmailController implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

	@Resource
	ClientRepository clientRepository;

	@Autowired
	EmailService emailService;

	private EmailResponse emailResponse;

	
	@RequestMapping(value = "/sendemail", method = RequestMethod.POST)
	public ResponseEntity<EmailResponse> sendEmailToEmp(@RequestBody EmailRequest emailRequest)
			throws AttendanceException {
		logger.debug("************************************************************************");
		logger.debug("Entry : sendEmailToEmp::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");
		try {
			boolean sentStatus = emailService.sendEmailToClientsideEmp(emailRequest.getEmployeeId(), emailRequest.getReminderText());

			if (sentStatus) {
				emailResponse = new EmailResponse();
				emailResponse.setReminderStatus("Reminder sent successfully");
				emailResponse.setReminderSentDate(new Date());
			} else {
				emailResponse = new EmailResponse();
				emailResponse.setReminderStatus("Reminder sending failed");
				emailResponse.setReminderSentDate(null);
			}

			return new ResponseEntity<EmailResponse>(emailResponse, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof AttendanceException) {
				throw (AttendanceException) e;
			}
		} finally {
			logger.debug("************************************************************************");
			logger.debug("Exit : sendEmailToEmp::" + "::" + System.currentTimeMillis());
			logger.debug("************************************************************************");
		}
		throw new AttendanceException("Unexpected Error While executing sendEmailToEmp",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/sendemails", method = RequestMethod.POST)
	public ResponseEntity<EmailResponse> sendEmailToAllClientsideEmps(@RequestBody EmailRequest emailRequest)
			throws AttendanceException {
		logger.debug("************************************************************************");
		logger.debug("Entry : sendEmailToClient::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");
		try {
			boolean sentStatus = emailService.sendEmailToAllClientsideEmps(emailRequest.getClientId(), emailRequest.getReminderText());

			if (sentStatus) {
				emailResponse = new EmailResponse();
				emailResponse.setReminderStatus("Reminder sent successfully");
				emailResponse.setReminderSentDate(new Date());
			} else {
				emailResponse = new EmailResponse();
				emailResponse.setReminderStatus("Reminder sending failed");
				emailResponse.setReminderSentDate(null);
			}

			return new ResponseEntity<EmailResponse>(emailResponse, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof AttendanceException) {
				throw (AttendanceException) e;
			}
		} finally {
			logger.debug("************************************************************************");
			logger.debug("Exit : sendEmailToClient::" + "::" + System.currentTimeMillis());
			logger.debug("************************************************************************");
		}
		throw new AttendanceException("Unexpected Error While executing sendEmailToClient",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
