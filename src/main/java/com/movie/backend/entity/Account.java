package com.movie.backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "Accounts")
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Assuming your primary key is of type Long
	
	
	@Column(name = "user_id")
	private int user_id;
	
	
	@Column(name = "user")
	private String user;
	
	@Column(name = "pass")
	private String pass;
	
	
	

	public Account(int user_id, String user, String pass) {
		this.user_id = user_id;
		this.user = user;
		this.pass = pass;
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
	public String getUser() {
		return user;
	}




	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
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
