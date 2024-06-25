package com.movie.backend.entity;

import javax.persistence.*;



@Entity
@Table(name = "movies", schema = "public")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    // Constructors, getters, and setters
	
	public Movie(int id, String title) {		
		this.id = id;
		this.title = title;	
	}
	
	public Movie() {
		
	}


	/**
	 * @return the movieId
	 */
	public int getId() {
		return id;
	}


	/**
	 * @param movie_id the movie_id to set
	 */
	public void setMovieNumber(int id) {
		this.id = id;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	


}
