package com.movie.backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {

    @EmbeddedId
    private RatingId id;

    @Column(name = "rating")
    private Double rating;

    
    
    
    
    
    public Rating() {}

    public Rating(RatingId id, Double rating) {
        this.id = id;
        this.rating = rating;
    }

    public RatingId getId() {
        return id;
    }

    public void setId(RatingId id) {
        this.id = id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
