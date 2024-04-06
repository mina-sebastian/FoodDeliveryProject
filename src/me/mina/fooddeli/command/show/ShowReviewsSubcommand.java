package me.mina.fooddeli.command.show;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.DeliveryPersonRepositoryService;
import me.mina.fooddeli.daoservices.RestaurantRepositoryService;
import me.mina.fooddeli.daoservices.UserRepositoryService;
import me.mina.fooddeli.model.person.DeliveryPerson;
import me.mina.fooddeli.model.restaurant.Restaurant;
import me.mina.fooddeli.model.review.Review;
import me.mina.fooddeli.utils.Utils;

public class ShowReviewsSubcommand extends Command {

    private RestaurantRepositoryService restaurantRepositoryService;
    private DeliveryPersonRepositoryService deliveryPersonRepositoryService;

    public ShowReviewsSubcommand(RestaurantRepositoryService restaurantRepositoryService,
                                 DeliveryPersonRepositoryService deliveryPersonRepositoryService) {
        super("reviews");

        this.restaurantRepositoryService = restaurantRepositoryService;
        this.deliveryPersonRepositoryService = deliveryPersonRepositoryService;
    }

    @Override
    public String execute() {
        System.out.println("Showing reviews...");
        System.out.println("Choose restaurant or delivery: ");
        String choice = getScanner().nextLine();
        switch (choice) {
            case "restaurant":
                StringBuilder sb = new StringBuilder();
                for(Restaurant restaurant: restaurantRepositoryService.getAll()){
                    sb.append(restaurant.getName()).append(":");
                    for (Review review: restaurant.getReviews()){
                        sb.append("\n").append("   ").append(review);
                    }
                    sb.append("\n");
                }
                return sb.toString();
            case "delivery":
                StringBuilder sb2 = new StringBuilder();
                for(DeliveryPerson deliveryPerson: deliveryPersonRepositoryService.getAll()){
                    sb2.append(deliveryPerson.getName()).append(":");
                    for (Review review: deliveryPerson.getReviews()){
                        sb2.append("\n").append("   ").append(review);
                    }
                    sb2.append("\n");
                }
                return sb2.toString();
            default:
                return "Invalid choice.";
        }
    }
}
