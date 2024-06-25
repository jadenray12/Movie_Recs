package com.movie.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movie.backend.entity.Rating;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
	@Query(value = "SELECT movie_id FROM recommend_movies_from_neighborhood(:inputUserId)", nativeQuery = true)
    List<Integer> recommendMoviesFromNeighborhood(Integer inputUserId);
	
	@Query("SELECT r FROM Rating r WHERE r.user_id = :userId AND r.movie_id = :movieId")
    Rating findByUserIdAndMovieId(@Param("userId") Long userId, @Param("movieId") Long movieId);
}
