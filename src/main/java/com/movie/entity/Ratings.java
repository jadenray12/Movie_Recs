package com.movie.entity;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
public class Ratings {
	
	
	@Column(name = "id")
	private int id;
	
	
	@Column(name = "user")
	private String user;
	
	
	
	
	@Column(name = "pass")
	private String pass;

}
