package com.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;



@Entity
public class UserData {
	@Id  //id
    @GeneratedValue
	private Long id;
	private String phoneMD5;
	private boolean status;
	private boolean security;
	
	@Lob 
	@Column(name="CONTENT", length=512)
	private String shedule;
	     
	
	public UserData(){
			}
	public UserData(String phoneMD5){
		this.phoneMD5 = phoneMD5;
	}
	public UserData(String phoneMD5, String emailMD5, String passwordMD5, boolean status){
		this.phoneMD5 = phoneMD5;
		this.status = status;
	}
	public UserData(String phoneMD5, boolean status){
		this.phoneMD5 = phoneMD5;
		this.status = status;
	}
	public UserData(String phoneMD5, String shedule){
		this.phoneMD5 = phoneMD5;
		this.shedule = shedule;
	}
	
	public void setPhoneMD5(String phoneMD5){
		this.phoneMD5 = phoneMD5;
	}
	public String getPhoneMD5(){
		return this.phoneMD5;
	}
	
	public void setStatus(boolean status){
		this.status = status;
	}
	public boolean getStatus(){ 
		return this.status;
	}
	
	public void setShedule(String shedule){
		this.shedule = shedule;
	}
	public String getShedule(){
		return this.shedule;
	}
	
	public void setSecurity(boolean security){
		this.security = security;
	}
	
	public boolean getSecurity(){
		return this.security;
	}
	
	@Override
    public String toString() {
        return "phoneMD5 "+this.phoneMD5+"\n"+"status "+this.status+"\n"+"shedule "+this.shedule;
    }

}
