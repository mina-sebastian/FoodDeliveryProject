package me.mina.fooddeli.model;

import me.mina.fooddeli.model.user.Person;
import me.mina.fooddeli.model.user.User;

import java.util.Objects;

public class Review {

    private int id;
    private User user;
    private String text;
    private int rating;

    public Review(int id, User user, String text, int rating) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "user=" + user +
                ", text='" + text + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;
        return rating == review.rating
                && Objects.equals(user, review.user)
                && Objects.equals(text, review.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, text, rating);
    }
}