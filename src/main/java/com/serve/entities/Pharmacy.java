package com.serve.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="PHARMACY")
public class Pharmacy {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int Pid;
	private String imgage;
	private String pinCode;
	@Column(length=1000)
	private String address;
	
	@ManyToOne
	private User user;
	
	public int getPid() {
		return Pid;
	}
	public String getImgage() {
		return imgage;
	}
	public String getPinCode() {
		return pinCode;
	}
	public String getAddress() {
		return address;
	}
	public void setPid(int pid) {
		Pid = pid;
	}
	public void setImgage(String imgage) {
		this.imgage = imgage;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	

}
