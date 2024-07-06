package com.movie.backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
	
	@Id
	@Column(name = "user_id")
	private Integer user_id; // Assuming your primary key is of type Long
	
		
	@Column(name = "username")
	private String username;
	
	@Column(name = "pass")
	private String pass;
	
	
	

	public Account(Integer user_id, String username, String pass) {
		this.user_id = user_id;
		this.username = username;
		this.pass = pass;
	}
	
	public Account() {
		
	}




	/**
	 * @return the user_id
	 */
	public int getUser_id() {
		return user_id;
	}




	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}




	/**
	 * @return the user
	 */
	public String getUsername() {
		return username;
	}




	/**
	 * @param user the user to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}




	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}




	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
	
	
	
	
	
	

}
