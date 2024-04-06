package me.mina.fooddeli.dao;

import me.mina.fooddeli.model.order.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderItemDao implements Dao<OrderItem> {

    //Simulam baza de date
    private static List<OrderItem> orderItems = null;


    public OrderItemDao() {
        //TODO de inlocuit cu conexiunea la baza de date
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
    }

    @Override
    public Optional<OrderItem> get(int id) {
        //TODO De folosit baza de date
        return orderItems.stream()
                .filter(orderItem -> orderItem.getId() == id)
                .findFirst();
    }

    @Override
    public List<OrderItem> getAll() {
        //TODO De folosit baza de date
        return new ArrayList<>(orderItems);
    }

    @Override
    public void create(OrderItem order) {
        //TODO De folosit baza de date
        orderItems.add(order);
    }

    @Override
    public void update(int id, OrderItem orderItem) {
        Optional<OrderItem> orderItemOptional = get(id);
        //TODO De folosit baza de date
        if (orderItemOptional.isPresent()) {
            OrderItem orderItemToUpdate = orderItemOptional.get();
            orderItemToUpdate.setQuantity(orderItem.getQuantity());
            orderItemToUpdate.setItem(orderItem.getItem());
        }else{
            System.out.println("Update error: The orderItem with id " + id + " does not exist");
        }
    }


    @Override
    public void delete(OrderItem orderItem) {
        //TODO De folosit baza de date
        orderItems.remove(orderItem);
    }
}
