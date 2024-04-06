package me.mina.fooddeli.daoservices;

import me.mina.fooddeli.dao.UserDao;
import me.mina.fooddeli.model.restaurant.Restaurant;
import me.mina.fooddeli.model.user.PremiumUser;
import me.mina.fooddeli.model.user.User;

import java.util.List;
import java.util.Optional;

public class UserRepositoryService {
    private static UserDao userDao;
    private static List<User> users;
    public UserRepositoryService(){
        if(userDao == null){
            userDao = new UserDao();
        }

        if(users == null){
            users = userDao.getAll();
        }
    }

    public Optional<User> get(int id) {
        return users.stream()
                .filter(restaurant -> restaurant.getId() == id)
                .findFirst()
                .or(() -> userDao.get(id));
    }

    public Optional<PremiumUser> getPremium(int id) {
        return get(id)
                .filter(user -> user instanceof PremiumUser)
                .map(user -> (PremiumUser) user);
    }

    public List<User> getAllRegularUsers() {
        return users.stream()
                .filter(user -> !(user instanceof PremiumUser))
                .toList();
    }

    public List<User> getAll() {
        return users;
    }

    public List<PremiumUser> getAllPremiumUsers() {
        return users.stream()
                .filter(user -> user instanceof PremiumUser)
                .map(user -> (PremiumUser) user)
                .toList();
    }

    public void create(User user) {
        userDao.create(user);
        // If database fails, do not add to the list
        users.add(user);
    }

    public void update(int id, User user) {
        Optional<User> userToUpdateOptional = get(id);
        if(userToUpdateOptional.isPresent()){
            User userToUpdate = userToUpdateOptional.get();
            userToUpdate.setName(user.getName());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setUserInfo(user.getUserInfo());
            if(userToUpdate instanceof PremiumUser && user instanceof PremiumUser){
                ((PremiumUser) userToUpdate).setDiscountRate(((PremiumUser) user).getDiscountRate());
            }
            userDao.update(id, user);
        }
    }

    public void delete(User user) {
        userDao.delete(user);
        users.remove(user);
    }

    public void delete(int id) {
        Optional<User> userOptional = get(id);
        userOptional.ifPresent(user -> {
            userDao.delete(user);
            users.remove(user);
        });
    }

    public Optional<User> findUserByName(String name) {
        return users.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    public Optional<User> getUserByCreds(String name, String password) {
        return users.stream()
                .filter(user -> user.getName().equals(name) && user.getPassword().equals(password))
                .findFirst();
    }

}
