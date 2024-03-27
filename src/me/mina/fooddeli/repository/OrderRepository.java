package me.mina.fooddeli.repository;

import me.mina.fooddeli.dao.OrderDaoService;
import me.mina.fooddeli.model.order.Order;

import java.util.List;
import java.util.Optional;

public class OrderRepository {

    private static OrderDaoService orderDaoService;

    private static List<Order> orders;

    public OrderRepository() {
        if (orderDaoService == null) {
            orderDaoService = new OrderDaoService();
        }

        if (orders == null) {
            orders = orderDaoService.getAll();
        }
    }

    public Optional<Order> get(int id) {
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .or(() -> orderDaoService.get(id));
    }

    public List<Order> getAll() {
        return orders;
    }

    public void create(Order order) {
        orderDaoService.create(order);
        // If database fails, do not add to the list
        orders.add(order);
    }

    public void update(int id, Order order) {
        Optional<Order> orderOptional = get(id);
        if (orderOptional.isPresent()) {
            Order orderToUpdate = orderOptional.get();
            orderToUpdate.setStatus(order.getStatus());
            orderToUpdate.setItems(order.getItems());
            orderToUpdate.setUserInfo(order.getUserInfo());
            orderDaoService.update(id, order);
        }
    }

    public void delete(Order order) {
        orderDaoService.delete(order);
        // If database fails, do not delete from the list
        orders.remove(order);
    }


}
