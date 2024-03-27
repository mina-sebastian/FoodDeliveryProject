package me.mina.fooddeli.model;

public interface Reviewable {
    void addReview(Review review);
    double getAverageRating();
}