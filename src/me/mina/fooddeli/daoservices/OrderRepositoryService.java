package me.mina.fooddeli.daoservices;

import me.mina.fooddeli.FoodDeliveryService;
import me.mina.fooddeli.dao.OrderDao;
import me.mina.fooddeli.dao.OrderItemDao;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.order.OrderItem;
import me.mina.fooddeli.model.order.OrderStatus;
import me.mina.fooddeli.model.restaurant.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderRepositoryService {
    private static OrderDao orderDao;
    private static List<Order> orders;

    public OrderRepositoryService() {
        if (orderDao == null) {
            orderDao = new OrderDao();
        }

        if (orders == null) {
            orders = orderDao.getAll();
        }
    }

    public Optional<Order> get(int id) {
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .or(() -> orderDao.get(id));
    }

    public List<Order> getAll() {
        return orders;
    }

    public int create(Order order, Restaurant restaurant) {
        int orderId = orderDao.create(order);
        order.setId(orderId);
        orders.add(order);

        FoodDeliveryService.getRestaurantRepositoryService()
                .addOrder(restaurant.getId(), order);
        return orderId;
    }

    public void update(int id, Order order) {
        Optional<Order> orderOptional = get(id);
        if (orderOptional.isPresent()) {
            Order orderToUpdate = orderOptional.get();
            orderToUpdate.setStatus(order.getStatus());
            orderToUpdate.setItems(order.getItems());
            orderToUpdate.setUserInfo(order.getUserInfo());
            orderDao.update(id, order);
        }
    }

    public void delete(Order order) {
        orderDao.delete(order);
        // If database fails, do not delete from the list
        FoodDeliveryService.getDeliveryPersonRepositoryService().deleteOrder(order.getId());
        FoodDeliveryService.getRestaurantRepositoryService().removeOrderFromAny(order.getId());
        orders.remove(order);
        order.getItems().forEach(item -> FoodDeliveryService.getOrderItemDao().delete(item));
    }

    public void delete(int id) {
        Optional<Order> orderOptional = get(id);
        orderOptional.ifPresent(this::delete);
    }





    public boolean cancelOrder(int orderId) {
        Optional<Order> orderOptional = get(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (!order.getStatus().equals(OrderStatus.PREPARING) && !order.getStatus().equals(OrderStatus.DELIVERED)) {
                order.setStatus(OrderStatus.CANCELLED);
                update(orderId, order);
                return true;
            }
        }
        return false;
    }

    public List<Order> filterOrdersByStatus(OrderStatus status) {
        return orders.stream()
                .filter(order -> order.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        get(orderId).ifPresent(order -> {
            order.setStatus(newStatus);
            update(orderId, order);
        });
    }

    public List<Order> getOrdersByPhoneNumber(String phoneNumber) {
        return orders.stream()
                .filter(order -> order.getUserInfo().getPhoneNumber().equals(phoneNumber))
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersByPhoneNumberAndStatus(String phoneNumber, OrderStatus status) {
        return orders.stream()
                .filter(order ->
                           order.getUserInfo().getPhoneNumber().equals(phoneNumber)
                        && order.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public void addOrderItem(int orderId, OrderItem orderItem) {
        get(orderId).ifPresent(order -> {
            int orderItemId = FoodDeliveryService.getOrderItemDao().create(orderItem);
            orderItem.setId(orderItemId);
            order.getItems().add(orderItem);
            update(orderId, order);
        });
    }

    public void updateOrderItem(int orderItemId, OrderItem updatedOrderItem) {
        FoodDeliveryService.getOrderItemDao().update(orderItemId, updatedOrderItem);
    }

    public void deleteOrderItem(int orderItemId) {
        Optional<OrderItem> orderItemOptional = FoodDeliveryService.getOrderItemDao().get(orderItemId);
        orderItemOptional.ifPresent(orderItem -> {
            FoodDeliveryService.getOrderItemDao().delete(orderItem);
            get(orderItem.getId()).ifPresent(order -> {
                order.getItems().remove(orderItem);
                update(order.getId(), order);
            });
        });
    }
}
