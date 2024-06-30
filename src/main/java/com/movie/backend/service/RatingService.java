package com.movie.backend.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.backend.entity.Movie;
import com.movie.backend.entity.Rating;
import com.movie.backend.repository.MovieRepository;
import com.movie.backend.repository.RatingRepository;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    
    private final MovieRepository movieRepository;
    
   
    @Autowired
    public RatingService(RatingRepository ratingRepository, MovieRepository movieRepository) {
        this.ratingRepository = ratingRepository;
		this.movieRepository = movieRepository;
    }

    public Rating addRating(int user_id, int movieId, Double rating) {
        

        // Create a new Rating object
        Rating newRating = new Rating(user_id, movieId, rating);
        

        // Save the rating
        return ratingRepository.save(newRating);
    }
    
    
    public Rating addRating(Rating rating) {
        

      

        // Save the rating
        return ratingRepository.save(rating);
    }
    
    public Rating updateRating(int userId, int movieId, double rating) {
    	Rating existingRating = ratingRepository.findByUserIdAndMovieId(userId, movieId);

      
        // Update the existing rating
        existingRating.setRating(rating);
        ratingRepository.save(existingRating);
        
        return existingRating;
    
    }
    
    public void deleteRating(int ratingId) {
        // Retrieve the existing rating by its ID
        Rating existingRating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found with id: " + ratingId));

        // Delete the rating
        ratingRepository.delete(existingRating);
    }
    
    public List<Movie> recommendMoviesFromNeighborhood(Integer inputUserId) {
        List<Integer> x = ratingRepository.recommendMoviesFromNeighborhood(inputUserId);
        
        MovieService movieService = new MovieService(movieRepository);
        List<Movie> movies = new ArrayList<Movie>();
        for (Integer movieNumber: x) {
      	
        	Movie movie = movieService.MovieNumber(movieNumber);
        	
        	movies.add(movie);
        	
        	
        	
        	
        	
        	
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
