package me.mina.fooddeli.dao;

import me.mina.fooddeli.model.restaurant.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemDao implements Dao<MenuItem> {

    //Simulam baza de date
    private static List<MenuItem> menuItems = null;


    public MenuItemDao() {
        //TODO de inlocuit cu conexiunea la baza de date
        if (menuItems == null) {
            menuItems = new ArrayList<>();
        }
    }

    @Override
    public Optional<MenuItem> get(int id) {
        //TODO De folosit baza de date
        return menuItems.stream()
                .filter(menuItem -> menuItem.getId() == id)
                .findFirst();
    }

    @Override
    public List<MenuItem> getAll() {
        //TODO De folosit baza de date
        return new ArrayList<>(menuItems);
    }

    @Override
    public void create(MenuItem menuItem) {
        //TODO De folosit baza de date
        menuItems.add(menuItem);
    }

    @Override
    public void update(int id, MenuItem menuItem) {
        Optional<MenuItem> menuItemOptional = get(id);
        //TODO De folosit baza de date
        if (menuItemOptional.isPresent()) {
            MenuItem menuItemToUpdate = menuItemOptional.get();
            menuItemToUpdate.setName(menuItem.getName());
            menuItemToUpdate.setPrice(menuItem.getPrice());
            menuItemToUpdate.setIngredients(menuItem.getIngredients());
        }else{
            System.out.println("Update error: The menuItem with id " + id + " does not exist");

        }
    }


    @Override
    public void delete(MenuItem menuItem) {
        //TODO De folosit baza de date
        menuItems.remove(menuItem);
    }
}
