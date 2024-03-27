package me.mina.fooddeli.model.user;

import me.mina.fooddeli.model.Review;
import me.mina.fooddeli.model.Reviewable;
import me.mina.fooddeli.utils.Utils;
import me.mina.fooddeli.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeliveryPerson extends Person implements Comparable<DeliveryPerson>, Reviewable {
    private Order currentOrder;
    private List<Order> ordersHistory;

    private List<Review> reviews;
    private double averageRating;

    public DeliveryPerson(String name, String password,
                          Order currentOrder, List<Order> ordersHistory,
                          List<Review> reviews, double averageRating) {
        super(name, password);
        this.currentOrder = currentOrder;
        this.ordersHistory = ordersHistory;
        this.reviews = reviews;
        this.averageRating = averageRating;
    }

    public DeliveryPerson(String name, String password) {
        super(name, password);
        reviews = new ArrayList<>();
        ordersHistory = new ArrayList<>();
        averageRating = 0;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public List<Order> getOrdersHistory() {
        return ordersHistory;
    }

    public void setOrdersHistory(List<Order> ordersHistory) {
        this.ordersHistory = ordersHistory;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        averageRating = Utils.reviewsToRating(reviews);
    }

    @Override
    public void addReview(Review review) {
        reviews.add(review);
        averageRating = Utils.reviewsToRating(reviews);
    }

    public double getAverageRating() {
        return averageRating;
    }

    @Override
    public int compareTo(DeliveryPerson o) {
        return Math.round((float)(o.getAverageRating()-averageRating));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryPerson that)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(averageRating, that.averageRating) == 0
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getPassword(), that.getPassword())
                && Objects.equals(currentOrder, that.currentOrder)
                && Objects.equals(Utils.objectListToString(ordersHistory),
                                  Utils.objectListToString(that.ordersHistory))
                && Objects.equals(reviews, that.reviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), currentOrder, ordersHistory, reviews, averageRating);
    }

    @Override
    public String toString() {
        return "DeliveryPerson{" +
                "name='" + getName() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", currentOrder=" + currentOrder +
                ", ordersHistory=" + Utils.objectListToString(ordersHistory) +
                ", reviews=" + Utils.objectListToString(reviews) +
                ", averageRating=" + averageRating +
                '}';
    }
}