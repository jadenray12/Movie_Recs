package com.movie.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movie.backend.entity.Rating;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    
	@Query(value = "SELECT movie_id FROM recommend_movies_from_neighborhood(:inputUserId)", nativeQuery = true)
    List<Integer> recommendMoviesFromNeighborhood(Integer inputUserId);
	
	@Query("SELECT r FROM Rating r WHERE r.user_id = :userId AND r.movie_id = :movieId")
    Rating findByUserIdAndMovieId(@Param("userId") int userId, @Param("movieId") int movieId);
	
	@Query("SELECT m.title, r.rating FROM Rating r JOIN Movie m ON r.movie_id = m.id WHERE r.user_id = :userId")
	List<Object[]> findRatingsByUserId(@Param("userId") int userId);
	

}
