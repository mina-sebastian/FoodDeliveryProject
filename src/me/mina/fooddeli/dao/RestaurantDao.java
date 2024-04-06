package me.mina.fooddeli.dao;

import me.mina.fooddeli.model.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantDao implements Dao<Restaurant> {

    //Simulam baza de date
    private static List<Restaurant> restaurants = null;

    public RestaurantDao() {
        //TODO de inlocuit cu conexiunea la baza de date
        if (restaurants == null) {
            restaurants = new ArrayList<>();
        }
    }

    @Override
    public Optional<Restaurant> get(int id) {
        //TODO De folosit baza de date
        return restaurants.stream()
                .filter(obj -> obj.getId() == id)
                .findFirst();
    }

    @Override
    public List<Restaurant> getAll() {
        //TODO De folosit baza de date
        return new ArrayList<>(restaurants);
    }

    @Override
    public void create(Restaurant obj) {
        //TODO De folosit baza de date
        restaurants.add(obj);
    }

    @Override
    public void update(int id, Restaurant obj) {
        Optional<Restaurant> objOptional = get(id);
        //TODO De folosit baza de date
        if (objOptional.isPresent()) {
            Restaurant objToUpdate = objOptional.get();
            objToUpdate.setName(obj.getName());
            objToUpdate.setMenu(obj.getMenu());
            objToUpdate.setQueue(obj.getQueue());
            objToUpdate.setReviews(obj.getReviews());
        }else{
            System.out.println("Update error: Restaurant with id " + id + " not found");
        }
    }


    @Override
    public void delete(Restaurant obj) {
        //TODO De folosit baza de date
        restaurants.remove(obj);
    }
}
