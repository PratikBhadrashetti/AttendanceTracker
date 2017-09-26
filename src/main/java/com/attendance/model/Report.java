package com.attendance.model;

/**
 * @author harsha.patil
 *
 */

public class Report {

	private int client_id;
	private String month;
	private String year;


	public Report(int client_id, String month, String year) {
		super();
		this.client_id = client_id;
		this.month = month;
		this.year = year;
	}

	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	
}
