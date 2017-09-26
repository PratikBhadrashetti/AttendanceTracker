package com.attendance.entity;

/**
 * @author harsha.patil
 *
 */

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Entity(name = "employee")
public class Employee {

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "empid")
	private String empid;
	
	@Column(name = "manager_id")
	private String managerid;
	
	@Column
	private String name;

	@Column
	private String emailid;

	@Column
	private String submitStatus;
	
	/*@Column
	private String phonenumber;*/

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Attendance> empAttendances;

	@ManyToOne
	@JoinColumn(name = "client_id")
	@JsonBackReference
	private Client client;

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	
	
	
	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}
	
	
	public String getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Set<Attendance> getEmpAttendances() {
		return empAttendances;
	}

	public void setEmpAttendances(Set<Attendance> empAttendances) {
		this.empAttendances = empAttendances;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	/*public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}*/
	
}
