package me.mina.fooddeli.dao;

import me.mina.fooddeli.model.person.User;
import me.mina.fooddeli.model.person.PremiumUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDao implements Dao<User> {

    //Simulam baza de date
    private static List<User> users = null;

    public UserDao() {
        //TODO de inlocuit cu conexiunea la baza de date
        if (users == null) {
            users = new ArrayList<>();
        }
    }

    @Override
    public Optional<User> get(int id) {
        //TODO De folosit baza de date
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public List<User> getAll() {
        //TODO De folosit baza de date
        return new ArrayList<>(users);
    }

    @Override
    public void create(User user) {
        //TODO De folosit baza de date
        users.add(user);
    }

    @Override
    public void update(int id, User user) {
        //TODO De folosit baza de date
        Optional<User> existingUser = get(id);
        if (existingUser.isPresent()) {
            User actualUser = existingUser.get();
            actualUser.setName(user.getName());
            actualUser.setPassword(user.getPassword());
            actualUser.setUserInfo(user.getUserInfo());
            if (actualUser instanceof PremiumUser && user instanceof PremiumUser) {
                ((PremiumUser) actualUser).setDiscountRate(((PremiumUser) user).getDiscountRate());
            }
        }else{
            System.out.println("Update error: User with id " + id + " not found");
        }

    }

    @Override
    public void delete(User user) {
        //TODO De folosit baza de date
        users.remove(user);
    }

    public void createPremiumUser(PremiumUser premiumUser) {
        create(premiumUser);
    }

    public Optional<PremiumUser> getPremiumUser(int id) {
        return get(id)
                .filter(user -> user instanceof PremiumUser)
                .map(user -> (PremiumUser) user);
    }

    public List<PremiumUser> getAllPremiumUsers() {
        return users.stream()
                .filter(user -> user instanceof PremiumUser)
                .map(user -> (PremiumUser) user)
                .collect(Collectors.toList());
    }
}
