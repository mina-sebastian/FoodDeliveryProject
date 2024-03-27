package me.mina.fooddeli.model.restaurant;

import me.mina.fooddeli.model.Review;
import me.mina.fooddeli.model.Reviewable;
import me.mina.fooddeli.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuItem implements Comparable<MenuItem>, Reviewable {

    private int id;
    private String name;
    private double price;
    private List<String> ingredients;
    private List<Review> reviews;
    private double averageRating;


    public MenuItem(int id, String name, double price, List<String> ingredients, List<Review> reviews, double averageRating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.reviews = reviews;
        this.averageRating = averageRating;
    }
    public MenuItem(int id, String name, double price, List<String> ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.reviews = new ArrayList<>();
        this.averageRating = 0;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        averageRating = Utils.reviewsToRating(reviews);
    }

    public void addReview(Review review) {
        reviews.add(review);
        averageRating = Utils.reviewsToRating(reviews);
    }

    public double getAverageRating() {
        return averageRating;
    }

    @Override
    public int compareTo(MenuItem o) {
        return Math.round((float)(o.getAverageRating()-averageRating));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem menuItem)) return false;
        return  id == menuItem.id
                && Double.compare(price, menuItem.price) == 0
                && Double.compare(averageRating, menuItem.averageRating) == 0
                && Objects.equals(name, menuItem.name)
                && Objects.equals(
                        Utils.objectListToString(ingredients),
                        Utils.objectListToString(menuItem.ingredients))
                && Objects.equals(
                        Utils.objectListToString(reviews),
                        Utils.objectListToString(menuItem.reviews));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, ingredients, reviews, averageRating);
    }


    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                "name='" + name + '\'' +
                ", price=" + price +
                ", ingredients=" + Utils.objectListToString(ingredients) +
                ", reviews=" + Utils.objectListToString(reviews) +
                ", averageRating=" + averageRating +
                '}';
    }
}