package me.mina.fooddeli.model.user;

import me.mina.fooddeli.utils.Utils;
import me.mina.fooddeli.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Person {
    private UserInfo userInfo;

    public User(String name, String password, UserInfo userInfo) {
        super(name, password);
        this.userInfo = userInfo;
    }

    public User(int id, String name, String password, UserInfo userInfo) {
        super(id, name, password);
        this.userInfo = userInfo;
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
        if (!(o instanceof User user)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(userInfo, user.userInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userInfo);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + getName() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
}