package com.movie.backend.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RatingId implements Serializable {

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "movie_id")
    private int movie_id;

    public RatingId() {}

    public RatingId(int userId, int movieId) {
        this.user_id = userId;
        this.movie_id = movieId;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }

    public int getMovieId() {
        return movie_id;
    }

    public void setMovieId(int movieId) {
        this.movie_id = movieId;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        RatingId that = (RatingId) o;
//        return userId == that.userId && movieId == that.movieId;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(userId, movieId);
//    }
}
