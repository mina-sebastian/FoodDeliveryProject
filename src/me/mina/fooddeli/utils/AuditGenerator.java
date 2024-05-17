package me.mina.fooddeli.utils;

import me.mina.fooddeli.FoodDeliveryService;
import me.mina.fooddeli.daoservices.*;
import me.mina.fooddeli.model.person.User;
import me.mina.fooddeli.model.person.PremiumUser;
import me.mina.fooddeli.model.restaurant.Restaurant;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.person.DeliveryPerson;

import java.util.List;

import static me.mina.fooddeli.utils.Constants.AUDIT_FILE;

public class AuditGenerator {


    public static void generateAuditFile() {
        System.out.println("Generating audit file");
        appendUsersSection();
        appendRestaurantsSection();
        appendOrdersSection();
        appendDeliveryPersonsSection();
    }

    private static void appendUsersSection() {
        UserRepositoryService service = FoodDeliveryService.getUserRepositoryService();
        List<User> users = service.getAll();
        FileManagement.scriereFisierChar(AUDIT_FILE, "Users Section");
        for (User user : users) {
            FileManagement.scriereFisierChar(AUDIT_FILE, user.toString());
        }

        List<PremiumUser> premiumUsers = service.getAllPremiumUsers();
        FileManagement.scriereFisierChar(AUDIT_FILE, "Premium Users Section");
        for (PremiumUser premiumUser : premiumUsers) {
            FileManagement.scriereFisierChar(AUDIT_FILE, premiumUser.toString());
        }
    }

    private static void appendRestaurantsSection() {
        RestaurantRepositoryService service = FoodDeliveryService.getRestaurantRepositoryService();
        List<Restaurant> restaurants = service.getAll();
        FileManagement.scriereFisierChar(AUDIT_FILE, "Restaurants Section");
        for (Restaurant restaurant : restaurants) {
            FileManagement.scriereFisierChar(AUDIT_FILE, restaurant.toString());
        }
    }

    private static void appendOrdersSection() {
        OrderRepositoryService service = FoodDeliveryService.getOrderRepositoryService();
        List<Order> orders = service.getAll();
        FileManagement.scriereFisierChar(AUDIT_FILE, "Orders Section");
        for (Order order : orders) {
            FileManagement.scriereFisierChar(AUDIT_FILE, order.toString());
        }
    }

    private static void appendDeliveryPersonsSection() {
        DeliveryPersonRepositoryService service = FoodDeliveryService.getDeliveryPersonRepositoryService();
        List<DeliveryPerson> deliveryPersons = service.getAll();
        FileManagement.scriereFisierChar(AUDIT_FILE, "Delivery Persons Section");
        for (DeliveryPerson deliveryPerson : deliveryPersons) {
            FileManagement.scriereFisierChar(AUDIT_FILE, deliveryPerson.toString());
        }
    }
}
