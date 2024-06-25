package com.movie.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.movie.backend.entity.Rating;
import com.movie.backend.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")  // Base URL mapping for all endpoints in this controller
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }


    // Example endpoint to save a new rating
    @PostMapping("/add")
    public Rating addRating(@RequestBody Rating rating) {
        return ratingService.addRating(rating);
    }

    // Example endpoint to update an existing rating
    @PutMapping("/update")
    public Rating updateRating(@RequestParam Long userId, @RequestParam Long movieId, @RequestParam int rating) {
    	
    	return ratingService.updateRating(userId, movieId, rating);
    }

    // Example endpoint to delete a rating by ID
    @DeleteMapping("/delete/{id}")
    public void deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
    }
}
