package me.mina.fooddeli.command.show;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.DeliveryPersonRepositoryService;
import me.mina.fooddeli.daoservices.OrderRepositoryService;
import me.mina.fooddeli.utils.Utils;

public class ShowDeliveryPersonSubcommand extends Command {

    private DeliveryPersonRepositoryService deliveryPersonRepositoryService;

    public ShowDeliveryPersonSubcommand(DeliveryPersonRepositoryService deliveryPersonRepositoryService) {
        super("delivery");

        this.deliveryPersonRepositoryService = deliveryPersonRepositoryService;
    }

    @Override
    public String execute() {
        System.out.println("Showing delivery persons...");
        return Utils.objectListToString(deliveryPersonRepositoryService.getAll());
    }
}
