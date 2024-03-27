package me.mina.fooddeli.model.user;

import me.mina.fooddeli.utils.Utils;
import me.mina.fooddeli.model.order.Order;

import java.util.List;
import java.util.Objects;

public class PremiumUser extends User {
    private double discountRate;

    public PremiumUser(String name, String password, UserInfo userInfo, List<Order> orderHistory, double discountRate) {
        super(name, password, userInfo, orderHistory);
        this.discountRate = discountRate;
    }

    public PremiumUser(String name, String password, UserInfo userInfo, double discountRate) {
        super(name, password, userInfo);
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PremiumUser that)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(discountRate, that.discountRate) == 0
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getPassword(), that.getPassword())
                && Objects.equals(getUserInfo(), that.getUserInfo())
                && Objects.equals(Utils.objectListToString(getOrderHistory()), Utils.objectListToString(that.getOrderHistory()));

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), discountRate);
    }

    @Override
    public String toString() {
        return "PremiumUser{" +
                "name='" + getName() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", userInfo=" + getUserInfo() +
                ", orderHistory=" + Utils.objectListToString(getOrderHistory()) +
                ", discountRate=" + discountRate +
                '}';
    }
}