package me.mina.fooddeli.command.create;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.DeliveryPersonRepositoryService;
import me.mina.fooddeli.daoservices.OrderRepositoryService;
import me.mina.fooddeli.daoservices.RestaurantRepositoryService;
import me.mina.fooddeli.daoservices.UserRepositoryService;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.order.OrderItem;
import me.mina.fooddeli.model.order.OrderStatus;
import me.mina.fooddeli.model.person.DeliveryPerson;
import me.mina.fooddeli.model.person.User;
import me.mina.fooddeli.model.restaurant.MenuItem;
import me.mina.fooddeli.model.restaurant.Restaurant;
import me.mina.fooddeli.model.review.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreateReviewSubcommand extends Command {
    private RestaurantRepositoryService restaurantRepositoryService;
    private UserRepositoryService userRepositoryService;
    private DeliveryPersonRepositoryService deliveryPersonRepositoryService;

    public CreateReviewSubcommand(RestaurantRepositoryService restaurantRepositoryService,
                                  UserRepositoryService userRepositoryService,
                                  DeliveryPersonRepositoryService deliveryPersonRepositoryService) {
        super("review");

        this.restaurantRepositoryService = restaurantRepositoryService;
        this.userRepositoryService = userRepositoryService;
        this.deliveryPersonRepositoryService = deliveryPersonRepositoryService;
    }

    @Override
    public String execute() {
        System.out.println("Creating review...");

        System.out.println("Enter your username: ");
        String username = getScanner().nextLine();
        System.out.println("Enter your password: ");
        String password = getScanner().nextLine();

        Optional<User> userOptional = userRepositoryService.getUserByCreds(username, password);

        if (userOptional.isEmpty()) {
            return "User not found. Wrong credidentials.";
        }

        System.out.println("Choose delivery person or restaurant: ");
        String choice = getScanner().nextLine();

        String text;
        int rating;
        Review review;
        switch (choice) {
            case "delivery":
                System.out.println("Enter delivery person username: ");
                String deliveryPersonUsername = getScanner().nextLine();
                Optional<DeliveryPerson> deliveryPersonOptional = deliveryPersonRepositoryService.findByUsername(deliveryPersonUsername);
                if (deliveryPersonOptional.isEmpty()) {
                    return "Delivery person not found.";
                }

                System.out.println("Enter review: ");
                text = getScanner().nextLine();
                System.out.println("Enter rating(1-10): ");
                rating = getScanner().nextInt();
                review = new Review(userOptional.get(), text, rating);
                deliveryPersonOptional.get().addReview(review);
                return "Created review: " + review;
            case "restaurant":
                System.out.println("Enter restaurant name: ");
                String restaurantName = getScanner().nextLine();
                Optional<Restaurant> restaurantOptional = restaurantRepositoryService.findRestaurantByName(restaurantName);
                if (restaurantOptional.isEmpty()) {
                    return "Restaurant not found.";
                }

                System.out.println("Enter review: ");
                text = getScanner().nextLine();
                System.out.println("Enter rating(1-10): ");
                rating = getScanner().nextInt();
                review = new Review(userOptional.get(), text, rating);
                restaurantOptional.get().addReview(review);
                return "Created review: " + review;
            default:
                return "Invalid choice.";
        }
    }
}
