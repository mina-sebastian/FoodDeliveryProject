package me.mina.fooddeli.model.restaurant;

import me.mina.fooddeli.model.Review;
import me.mina.fooddeli.model.Reviewable;
import me.mina.fooddeli.utils.Utils;
import me.mina.fooddeli.model.order.Order;

import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Restaurant implements Comparable<Restaurant>, Reviewable {

    private int id;
    private String name;
    private List<MenuItem> menu;
    private List<Review> reviews;

    private Queue<Order> queue;

    private double averageRating;

    public Restaurant(int id, String name, List<MenuItem> menu, List<Review> reviews, Queue<Order> queue, double averageRating) {
        this.id = id;
        this.name = name;
        this.menu = menu;
        this.reviews = reviews;
        this.queue = queue;
        this.averageRating = averageRating;
    }

    public Restaurant(int id, String name, List<MenuItem> menu) {
        this.id = id;
        this.name = name;
        this.menu = menu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        averageRating = Utils.reviewsToRating(reviews);
    }

    public Queue<Order> getQueue() {
        return queue;
    }

    public void addReview(Review review) {
        reviews.add(review);
        averageRating = Utils.reviewsToRating(reviews);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant that)) return false;
        return id == that.id
                && Double.compare(averageRating, that.averageRating) == 0
                && Objects.equals(name, that.name)
                && Objects.equals(Utils.objectListToString(menu),
                                  Utils.objectListToString(that.menu))
                && Objects.equals(Utils.objectListToString(reviews),
                                  Utils.objectListToString(that.reviews));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, menu, reviews, queue, averageRating);
    }

    public double getAverageRating() {
        return averageRating;
    }

    @Override
    public int compareTo(Restaurant o) {
        return Math.round((float)(o.getAverageRating()-averageRating));
    }
}