package me.mina.fooddeli.model.order;

import me.mina.fooddeli.model.restaurant.MenuItem;

import java.util.Objects;

public class OrderItem {

    private int id;
    private MenuItem item;
    private int quantity;


    public OrderItem(int id, MenuItem item, int quantity) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem orderItem)) return false;
        return id == orderItem.id
                && quantity == orderItem.quantity
                && Objects.equals(item, orderItem.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                "item=" + item +
                ", quantity=" + quantity +
                '}';
    }
}