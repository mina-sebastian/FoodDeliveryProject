package me.mina.fooddeli.dao;

import me.mina.fooddeli.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDao implements Dao<Order> {

    //Simulam baza de date
    private static List<Order> orders = null;

    public OrderDao() {
        //TODO de inlocuit cu conexiunea la baza de date
        if (orders == null) {
            orders = new ArrayList<>();
        }
    }

    @Override
    public Optional<Order> get(int id) {
        //TODO De folosit baza de date
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst();
    }

    @Override
    public List<Order> getAll() {
        //TODO De folosit baza de date
        return new ArrayList<>(orders);
    }

    @Override
    public void create(Order order) {
        //TODO De folosit baza de date
        orders.add(order);
    }

    @Override
    public void update(int id, Order order) {
        Optional<Order> orderOptional = get(id);
        //TODO De folosit baza de date
        if (orderOptional.isPresent()) {
            Order orderToUpdate = orderOptional.get();
            orderToUpdate.setStatus(order.getStatus());
            orderToUpdate.setItems(order.getItems());
            orderToUpdate.setUserInfo(order.getUserInfo());
        }else{
            System.out.println("Update error: The order with id " + id + " does not exist");

        }
    }


    @Override
    public void delete(Order order) {
        //TODO De folosit baza de date
        orders.remove(order);
    }
}
