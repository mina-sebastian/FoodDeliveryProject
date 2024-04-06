package me.mina.fooddeli.command.show;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.OrderRepositoryService;
import me.mina.fooddeli.daoservices.RestaurantRepositoryService;
import me.mina.fooddeli.utils.Utils;

public class ShowOrderSubcommand extends Command {

    private OrderRepositoryService orderRepositoryService;

    public ShowOrderSubcommand(OrderRepositoryService orderRepositoryService) {
        super("orders");

        this.orderRepositoryService = orderRepositoryService;
    }

    @Override
    public String execute() {
        System.out.println("Showing restaurants...");
        return Utils.objectListToString(orderRepositoryService.getAll());
    }
}
