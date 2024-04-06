package me.mina.fooddeli.command.show;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.RestaurantRepositoryService;
import me.mina.fooddeli.daoservices.UserRepositoryService;
import me.mina.fooddeli.utils.Utils;

public class ShowRestaurantsSubcommand extends Command {

    private RestaurantRepositoryService restaurantRepositoryService;

    public ShowRestaurantsSubcommand(RestaurantRepositoryService restaurantRepositoryService) {
        super("restaurants");

        this.restaurantRepositoryService = restaurantRepositoryService;
    }

    @Override
    public String execute() {
        System.out.println("Showing restaurants...");
        return Utils.objectListToString(restaurantRepositoryService.getAll());
    }
}
