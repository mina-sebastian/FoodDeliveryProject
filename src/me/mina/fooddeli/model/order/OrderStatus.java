package me.mina.fooddeli.model.order;

public enum OrderStatus {
    NOT_SUBMITTED("Not Submitted"),
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PREPARING("Preparing"),
    READY("Ready"),
    ON_THE_WAY("On the Way"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}