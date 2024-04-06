package me.mina.fooddeli.model.person;

import me.mina.fooddeli.model.review.Review;
import me.mina.fooddeli.model.review.Reviewable;
import me.mina.fooddeli.utils.Utils;
import me.mina.fooddeli.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeliveryPerson extends Person implements Comparable<DeliveryPerson>, Reviewable {
    private Order currentOrder;
    private List<Review> reviews;

    public DeliveryPerson(int id, String name, String password,
                          Order currentOrder,
                          List<Review> reviews) {
        super(id, name, password);
        this.currentOrder = currentOrder;
        this.reviews = reviews;
    }

    public DeliveryPerson(String name, String password,
                          Order currentOrder,
                          List<Review> reviews) {
        super(name, password);
        this.currentOrder = currentOrder;
        this.reviews = reviews;
    }

    public DeliveryPerson(int id, String name, String password) {
        super(id, name, password);
        reviews = new ArrayList<>();
    }

    public DeliveryPerson(String name, String password) {
        super(name, password);
        reviews = new ArrayList<>();
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }


    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public void removeReview(Review review) {
        reviews.remove(review);
    }

    public double getAverageRating() {
        return Utils.reviewsToRating(reviews);
    }

    @Override
    public int compareTo(DeliveryPerson o) {
        return Math.round((float)(o.getAverageRating()-getAverageRating()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryPerson that)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(getAverageRating(), that.getAverageRating()) == 0
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getPassword(), that.getPassword())
                && Objects.equals(currentOrder, that.currentOrder)
                && Objects.equals(reviews, that.reviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), currentOrder, reviews, getAverageRating());
    }

    @Override
    public String toString() {
        return "DeliveryPerson{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", currentOrder=" + currentOrder +
                ", reviews=" + Utils.objectListToString(reviews) +
                ", averageRating=" + getAverageRating() +
                '}';
    }
}