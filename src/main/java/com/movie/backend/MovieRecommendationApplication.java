package com.movie.backend;


import java.security.Provider.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.movie.backend.entity.Movie;
import com.movie.backend.service.MovieService;

@SpringBootApplication
@ComponentScan("com.movie")// Scan all components in the com.movie package and its sub-packages
@ComponentScan("com.movie.entity") 
public class MovieRecommendationApplication {

    @Autowired
    private MovieService movieService;

    public static void main(String[] args) {
        //SpringApplication.run(MovieRecommendationApplication.class, args);
        
    	ConfigurableApplicationContext context = SpringApplication.run(MovieRecommendationApplication.class, args);

        // Assuming you want to use the RatingService method here
        MovieService movieService = context.getBean(MovieService.class);
        
        Movie movie = movieService.MovieNumber((Integer)8);
        
        System.out.print(movie.getTitle());
        

        
    }

	
    
}
