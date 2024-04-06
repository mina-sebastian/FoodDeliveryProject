package me.mina.fooddeli.model.person;

import java.util.Objects;

public class PremiumUser extends User {
    private double discountRate;

    public PremiumUser(String name, String password, UserInfo userInfo, double discountRate) {
        super(name, password, userInfo);
        this.discountRate = discountRate;
    }

    public PremiumUser(int id, String name, String password, UserInfo userInfo, double discountRate) {
        super(id, name, password, userInfo);
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
                && Objects.equals(getUserInfo(), that.getUserInfo());

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
                ", discountRate=" + discountRate +
                '}';
    }
}