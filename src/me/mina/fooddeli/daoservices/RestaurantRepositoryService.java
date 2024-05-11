package me.mina.fooddeli.daoservices;

import me.mina.fooddeli.FoodDeliveryService;
import me.mina.fooddeli.dao.MenuItemDao;
import me.mina.fooddeli.dao.RestaurantDao;
import me.mina.fooddeli.dao.ReviewDao;
import me.mina.fooddeli.model.review.Review;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.restaurant.MenuItem;
import me.mina.fooddeli.model.restaurant.Restaurant;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RestaurantRepositoryService {

    private static RestaurantDao restaurantDao;


    private static List<Restaurant> restaurants;
    public RestaurantRepositoryService() {
        if(restaurantDao == null) {
            restaurantDao = new RestaurantDao();
        }



        if(restaurants == null) {
            restaurants = restaurantDao.getAll();
        }
    }

    public Optional<Restaurant> get(int id) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getId() == id)
                .findFirst()
                .or(() -> restaurantDao.get(id));
    }

    public List<Restaurant> getAll() {
        Collections.sort(restaurants);
        return restaurants;
    }

    public void create(Restaurant restaurant) {
        int id = restaurantDao.create(restaurant);
        restaurant.setId(id);
        // If database fails, do not add to the list
        restaurants.add(restaurant);
    }

    public void update(int id, Restaurant restaurant) {
        Optional<Restaurant> restaurantOptional = get(id);
        if(restaurantOptional.isPresent()) {
            Restaurant restaurantToUpdate = restaurantOptional.get();
            restaurantToUpdate.setName(restaurant.getName());
            restaurantToUpdate.setMenu(restaurant.getMenu());
            restaurantToUpdate.setReviews(restaurant.getReviews());
            restaurantToUpdate.setQueue(restaurant.getQueue());
            restaurantDao.update(id, restaurant);
        }
    }

    public void delete(Restaurant restaurant) {
        restaurantDao.delete(restaurant);
        // If database fails, do not delete from the list
        restaurants.remove(restaurant);
        restaurant.getMenu().forEach(menuItem -> FoodDeliveryService.getMenuItemDao().delete(menuItem));
        restaurant.getReviews().forEach(review -> FoodDeliveryService.getReviewDao().delete(review));
    }

    public void delete(int id) {
        Optional<Restaurant> restaurantOptional = get(id);
        restaurantOptional.ifPresent(this::delete);
    }


    // Menu Items
    public void addMenuItem(int restaurantId, MenuItem menuItem) {
        Optional<Restaurant> restaurantOptional = get(restaurantId);
        if(restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.getMenu().add(menuItem);
            int menuItemId = FoodDeliveryService.getMenuItemDao().create(menuItem);
            menuItem.setId(menuItemId);
            restaurantDao.update(restaurantId, restaurant);
        }
    }

    public void updateMenuItem(int restaurantId, MenuItem menuItem) {
        Optional<Restaurant> restaurantOptional = get(restaurantId);
        if(restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.getMenu().remove(menuItem);
            restaurant.getMenu().add(menuItem);
            FoodDeliveryService.getMenuItemDao().update(menuItem.getId(), menuItem);
            restaurantDao.update(restaurantId, restaurant);
        }
    }

    public void removeMenuItem(int restaurantId, MenuItem menuItem) {
        Optional<Restaurant> restaurantOptional = get(restaurantId);
        if(restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.getMenu().remove(menuItem);
            FoodDeliveryService.getMenuItemDao().delete(menuItem);
            restaurantDao.update(restaurantId, restaurant);
        }
    }

    // Orders
    public void addOrder(int restaurantId, Order order) {
        Optional<Restaurant> restaurantOptional = get(restaurantId);
        if(restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.getQueue().add(order);
            restaurantDao.update(restaurantId, restaurant);
        }
    }

    public void removeOrder(int restaurantId, Order order) {
        Optional<Restaurant> restaurantOptional = get(restaurantId);
        if(restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            if(restaurant.getQueue().contains(order)) {
                restaurant.getQueue().remove(order);
                FoodDeliveryService.getOrderRepositoryService().delete(order);
                restaurantDao.update(restaurantId, restaurant);
            }
        }
    }

    public void removeOrderFromAny(int orderId) {
        for (Restaurant restaurant : restaurants) {
            if(restaurant.getQueue().stream().anyMatch(order -> order.getId() == orderId)) {
                Order order = restaurant.getQueue().stream()
                        .filter(order1 -> order1.getId() == orderId).findFirst().get();
                restaurant.getQueue().remove(order);
                FoodDeliveryService.getOrderRepositoryService().delete(order);
                restaurantDao.update(restaurant.getId(), restaurant);
            }
        }
    }

    // Reviews
    public void addReview(int restaurantId, Review review) {
        Optional<Restaurant> restaurantOptional = get(restaurantId);
        if(restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.addReview(review);
            int reviewId = FoodDeliveryService.getReviewDao().create(review);
            review.setId(reviewId);
            restaurantDao.update(restaurantId, restaurant);
        }
    }

    public void updateReview(int restaurantId, Review review) {
        Optional<Restaurant> restaurantOptional = get(restaurantId);
        if(restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.getReviews().remove(review);
            restaurant.getReviews().add(review);
            FoodDeliveryService.getReviewDao().update(review.getId(), review);
            restaurantDao.update(restaurantId, restaurant);
        }
    }

    public void removeReview(int restaurantId, Review review) {
        Optional<Restaurant> restaurantOptional = get(restaurantId);
        if(restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.getReviews().remove(review);
            FoodDeliveryService.getReviewDao().delete(review);
            restaurantDao.update(restaurantId, restaurant);
        }
    }

    public Optional<Restaurant> findRestaurantByName(String name) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getName().equals(name))
                .findFirst();
    }

    public Optional<MenuItem> findMenuItemByName(Restaurant restaurant, String name) {
        return restaurant.getMenu().stream()
                .filter(menuItem -> menuItem.getName().equalsIgnoreCase(name))
                .findFirst();
    }

}
