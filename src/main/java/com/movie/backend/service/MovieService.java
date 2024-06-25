package com.movie.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.movie.backend.entity.Movie;
import com.movie.backend.repository.MovieRepository;

import java.util.List;

@Service
public class MovieService {
    
    private final MovieRepository movieRepository;
    
    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    
    public Movie MovieNumber(Integer id) {
        return movieRepository.getMovieById(id);
    }
    
//    public List<Movie> findLatestMovie() {
//        return movieRepository.findFirstByOrderByMovie_IdDesc();
//    }
}
