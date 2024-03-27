package me.mina.fooddeli.model;

import me.mina.fooddeli.model.user.Person;

import java.util.Objects;

public class Review {
    private Person person;
    private String text;
    private int rating;

    public Review(Person person, String text, int rating) {
        this.person = person;
        this.text = text;
        this.rating = rating;
    }

    public Person getUser() {
        return person;
    }

    public void setUser(Person person) {
        this.person = person;
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
                "person=" + person +
                ", text='" + text + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;
        return rating == review.rating
                && Objects.equals(person, review.person)
                && Objects.equals(text, review.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, text, rating);
    }
}