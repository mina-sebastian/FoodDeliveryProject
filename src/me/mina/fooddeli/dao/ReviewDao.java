package me.mina.fooddeli.dao;

import me.mina.fooddeli.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDao implements Dao<Review>{

    //Simulam baza de date
    private static List<Review> reviews = null;

    public ReviewDao() {
        //TODO de inlocuit cu conexiunea la baza de date
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
    }

    @Override
    public Optional<Review> get(int id) {
        //TODO De folosit baza de date
        return reviews.stream()
                .filter(review -> review.getId() == id)
                .findFirst();
    }

    @Override
    public List<Review> getAll() {
        //TODO De folosit baza de date
        return new ArrayList<>(reviews);
    }

    @Override
    public void create(Review review) {
        //TODO De folosit baza de date
        reviews.add(review);
    }

    @Override
    public void update(int id, Review review) {
        Optional<Review> reviewOptional = get(id);
        //TODO De folosit baza de date
        if (reviewOptional.isPresent()) {
            Review reviewToUpdate = reviewOptional.get();
            reviewToUpdate.setRating(review.getRating());
            reviewToUpdate.setUser(review.getUser());
            reviewToUpdate.setRating(review.getRating());
            reviewToUpdate.setText(review.getText());
        }else{
            System.out.println("Update error: The review with id " + id + " does not exist");
        }
    }

    @Override
    public void delete(Review review) {
        //TODO De folosit baza de date
        reviews.remove(review);
    }
}
