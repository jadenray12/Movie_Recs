package com.movie.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movie.backend.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

	@Query("SELECT m FROM Movie m WHERE m.id = :movieId")
    Movie getMovieById(@Param("movieId") Integer movieId);

	@Query("SELECT m.id FROM Movie m WHERE m.title = :movie")
	int getMovieIdByMovie(@Param("movie")String movie);
}
