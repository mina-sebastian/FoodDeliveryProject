package me.mina.fooddeli.model.restaurant;

import me.mina.fooddeli.model.review.Review;
import me.mina.fooddeli.model.review.Reviewable;
import me.mina.fooddeli.utils.Utils;
import me.mina.fooddeli.model.order.Order;

import java.util.*;

public class Restaurant implements Comparable<Restaurant>, Reviewable {

    private int id;
    private String name;
    private List<MenuItem> menu;
    private List<Review> reviews;
    private Queue<Order> queue;


    public Restaurant(int id, String name, List<MenuItem> menu, List<Review> reviews, Queue<Order> queue, double averageRating) {
        this.id = id;
        this.name = name;
        this.menu = menu;
        this.reviews = reviews;
        this.queue = queue;
    }

    public Restaurant(String name, List<MenuItem> menu, List<Review> reviews, Queue<Order> queue, double averageRating) {
        this.name = name;
        this.menu = menu;
        this.reviews = reviews;
        this.queue = queue;
    }

    public Restaurant(int id, String name, List<MenuItem> menu) {
        this.id = id;
        this.name = name;
        this.menu = menu;
        this.reviews = new ArrayList<>();
        this.queue = new LinkedList<Order>();
    }

    public Restaurant(String name, List<MenuItem> menu) {
        this.name = name;
        this.menu = menu;
        this.reviews = new ArrayList<>();
        this.queue = new LinkedList<Order>();
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

    public void setQueue(Queue<Order> queue) {
        this.queue = queue;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Queue<Order> getQueue() {
        return queue;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public void removeReview(Review review) {
        reviews.remove(review);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant that)) return false;
        return id == that.id
                && Double.compare(getAverageRating(), that.getAverageRating()) == 0
                && Objects.equals(name, that.name)
                && Objects.equals(Utils.objectListToString(menu),
                                  Utils.objectListToString(that.menu))
                && Objects.equals(Utils.objectListToString(reviews),
                                  Utils.objectListToString(that.reviews));
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menu=" + Utils.objectListToString(menu) +
                ", reviews=" + Utils.objectListToString(reviews) +
                ", queue=" + Utils.queueToString(queue) +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, menu, reviews, queue, getAverageRating());
    }

    public double getAverageRating() {
        return Utils.reviewsToRating(reviews);
    }

    @Override
    public int compareTo(Restaurant o) {
        return Math.round((float)(o.getAverageRating()-getAverageRating()));
    }
}