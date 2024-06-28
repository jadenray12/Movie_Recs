package com.movie.backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int user_id; // Assuming your primary key is of type Long
	
	
	@Column(name = "movie_id")
	private int movie_id;
	
	
	
	
	@Column(name = "rating")
	private Double rating;
	
	
	
	public Rating(int user_id, int movie_id, Double rating) {
		this.user_id = user_id;
		this.movie_id = movie_id;
		this.rating = rating;
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
	 * @return the movie_id
	 */
	public int getMovie_id() {
		return movie_id;
	}



	/**
	 * @param movie_id the movie_id to set
	 */
	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}



	/**
	 * @return the rating
	 */
	public Double getRating() {
		return rating;
	}



	/**
	 * @param rating the rating to set
	 */
	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	
	
	

}


