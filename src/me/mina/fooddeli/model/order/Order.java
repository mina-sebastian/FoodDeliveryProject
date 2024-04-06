package me.mina.fooddeli.model.order;

import me.mina.fooddeli.utils.Utils;
import me.mina.fooddeli.model.user.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private int id;
    private List<OrderItem> items;
    private OrderStatus status;
    private UserInfo userInfo;

    public Order(int id, UserInfo userInfo, List<OrderItem> items, OrderStatus status) {
        this.id = id;
        this.userInfo = userInfo;
        this.items = items;
        this.status = status;
    }

    public Order(UserInfo userInfo, List<OrderItem> items, OrderStatus status) {
        this.userInfo = userInfo;
        this.items = items;
        this.status = status;
    }

    public Order(int id, UserInfo userInfo) {
        this.id = id;
        this.userInfo = userInfo;
        this.items = new ArrayList<>();
        this.status = OrderStatus.NOT_SUBMITTED;
    }

    public Order(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.items = new ArrayList<>();
        this.status = OrderStatus.NOT_SUBMITTED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return id == order.id
            && status == order.status
            && Objects.equals(userInfo.toString(), order.userInfo.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, items, status, userInfo);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", items=" + Utils.objectListToString(items) +
                ", status=" + status +
                ", userInfo=" + userInfo +
                '}';
    }
}