package me.mina.fooddeli.command.create;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.RestaurantRepositoryService;
import me.mina.fooddeli.model.restaurant.MenuItem;
import me.mina.fooddeli.model.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

public class CreateRestaurantSubcommand extends Command {

    RestaurantRepositoryService restaurantRepositoryService;

    public CreateRestaurantSubcommand(RestaurantRepositoryService restaurantRepositoryService) {
        super("restaurant");
        this.restaurantRepositoryService = restaurantRepositoryService;
    }

    @Override
    public String execute() {

        try{
            System.out.println("Creating restaurant...");
            System.out.println("Enter restaurant name: ");
            String name = getScanner().nextLine();
            System.out.println("Enter menu size: ");
            int menuSize = getScanner().nextInt();
            getScanner().nextLine();

            List<MenuItem> menu = new ArrayList<>();
            for (int i = 0; i < menuSize; i++) {
                System.out.println("Enter item " + (i+1) + " name: ");
                String itemName = getScanner().nextLine();
                System.out.println("Enter item " + (i+1) + "(" + itemName + ") price: ");
                double itemPrice = getScanner().nextDouble();
                getScanner().nextLine();
                System.out.println("Enter receipe list(separated by ','): ");
                String receipe = getScanner().nextLine();
                List<String> receipeList = Arrays.stream(receipe.split(",")).toList();
                menu.add(new MenuItem(itemName, itemPrice, receipeList));
            }

            Restaurant restaurant = new Restaurant(name, menu);
            restaurantRepositoryService.create(restaurant);
            return "Restaurant created: " + restaurant;
        }catch (InputMismatchException e){
            return "Error creating restaurant: Input mismatch exception. Please enter correct values.";
        }catch (Exception e){
            e.printStackTrace();
            return "Error creating restaurant";
        }
    }
}
