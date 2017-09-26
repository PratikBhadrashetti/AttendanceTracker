package com.attendance.model;

/**
 * @author harsha.patil
 *
 */

public class EmailRequest {

	private int employeeId;
	private int clientId;
	private String reminderText;

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getReminderText() {
		return reminderText;
	}

	public void setReminderText(String reminderText) {
		this.reminderText = reminderText;
	}

}
