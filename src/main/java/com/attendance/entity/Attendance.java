package com.attendance.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author harsha.patil
 *
 */

@Component
@Entity(name = "attendance")
public class Attendance implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private String month;

	@Column
	private String year;

	@Column
	private String submitStatus;

	@OneToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	private DateOfMonth date;
	
	@ManyToOne
	@JsonIgnore
	private Employee employee;
	

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public DateOfMonth getDate() {
		return date;
	}

	public void setDate(DateOfMonth date) {
		this.date = date;
	}

}
