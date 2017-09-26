package com.attendance.model;

import java.util.Date;

/**
 * @author harsha.patil
 *
 */

public class EmailResponse {

	private String reminderStatus;
	private Date reminderSentDate;

	

	public String getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(String reminderStatus) {
		this.reminderStatus = reminderStatus;
	}

	public Date getReminderSentDate() {
		return reminderSentDate;
	}

	public void setReminderSentDate(Date reminderSentDate) {
		this.reminderSentDate = reminderSentDate;
	}

}
