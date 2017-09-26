package com.attendance.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Entity(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private int id;

	@Column
	private String username;

	@Column
	private String password;
	
	@JsonIgnore

	@ManyToMany( cascade = CascadeType.ALL)
	@JoinTable(name = "client_user", joinColumns = { 
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "client_id", 
					nullable = false, updatable = false) })

	//@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	//@JsonManagedReference
	private List<Client> clients;
	
	@Column
	private String usermail;

	public String getUsermail() {
		return usermail;
	}

	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}


	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
