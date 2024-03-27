package me.mina.fooddeli.model.user;

import me.mina.fooddeli.utils.Utils;
import me.mina.fooddeli.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Person {
    private UserInfo userInfo;
    private List<Order> orderHistory;

    public User(String name, String password, UserInfo userInfo, List<Order> orderHistory) {
        super(name, password);
        this.userInfo = userInfo;
        this.orderHistory = orderHistory;
    }

    public User(String name, String password, UserInfo userInfo) {
        super(name, password);
        this.userInfo = userInfo;
        this.orderHistory = new ArrayList<>();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
    }

    public void removeOrderFromHistory(Order order) {
        orderHistory.remove(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(userInfo, user.userInfo)
                && Objects.equals(Utils.objectListToString(orderHistory),
                                    Utils.objectListToString(user.orderHistory));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userInfo, orderHistory);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + getName() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", userInfo=" + userInfo +
                ", orderHistory=" + Utils.objectListToString(orderHistory) +
                '}';
    }
}