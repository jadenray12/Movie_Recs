package com.movie.backend.service;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie.backend.entity.Movie;
import com.movie.backend.entity.Rating;
import com.movie.backend.entity.RatingId;
import com.movie.backend.repository.MovieRepository;
import com.movie.backend.repository.RatingRepository;

@Service
public class RatingService {

    private RatingRepository ratingRepository;
    
    private MovieRepository movieRepository;
    
   
    @Autowired
    public RatingService(RatingRepository ratingRepository, MovieRepository movieRepository) {
        this.ratingRepository = ratingRepository;
		this.movieRepository = movieRepository;
    }

    @Transactional
    public Rating addRating(Integer user_id, Integer movieId, Double rating) {  
    	RatingId ratingId = new RatingId(user_id, movieId);
        Rating newrating = new Rating(ratingId, rating);
        return ratingRepository.save(newrating);
    }
    
    
    
    public Rating updateRating(int userId, int movieId, double rating) {
    	Rating existingRating = ratingRepository.findByUserIdAndMovieId(userId, movieId);

      
        // Update the existing rating
        existingRating.setRating(rating);
        ratingRepository.save(existingRating);
        
        return existingRating;
    
    }
    
    public void deleteRating(int userId, int movieId) {
        
    	Rating existingRating = ratingRepository.findByUserIdAndMovieId(userId, movieId);
    	
        ratingRepository.delete(existingRating);
    }
    
    public Map<String,Double> recommendMoviesFromNeighborhood(Integer inputUserId) {
        List<Object[]> x = ratingRepository.recommendMoviesFromNeighborhood(inputUserId);
        
        
        Map<String, Double> movies = new HashMap<>();
    	
    	for (Object[] result : x) {
            String title = (String) result[0];
            Double rating = (Double) result[1];
            movies.put(title, rating);
        }
        	
        return movies;
        
        
    }
    
    public Map<String, Double> getRatingsByUserId(Integer inputUserId){
    	List<Object[]> x = ratingRepository.findRatingsByUserId(inputUserId);
    	
    	if (x.size() == 0) {
    		return null;
    	}
    	
    	
    	Map<String, Double> ratings = new HashMap<>();
    	
    	for (Object[] result : x) {
            String title = (String) result[0];
            Double rating = (Double) result[1];
            ratings.put(title, rating);
        }
    	
    	
    	return ratings;
    }
}
