package me.mina.fooddeli.command.create;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.DeliveryPersonRepositoryService;
import me.mina.fooddeli.model.person.DeliveryPerson;

public class CreateDeliveryPersonSubcommand extends Command {

    DeliveryPersonRepositoryService deliveryPersonRepositoryService;

    public CreateDeliveryPersonSubcommand(DeliveryPersonRepositoryService deliveryPersonRepositoryService) {
        super("delivery");
        this.deliveryPersonRepositoryService = deliveryPersonRepositoryService;
    }

    @Override
    public String execute() {

        try{
            System.out.println("Creating user...");
            System.out.println("Enter username: ");
            String username = getScanner().nextLine();
            System.out.println("Enter password: ");
            String password = getScanner().nextLine();

            DeliveryPerson deliveryPerson = new DeliveryPerson(username, password);


            deliveryPersonRepositoryService.create(deliveryPerson);

            return "Delivery person created: " + deliveryPerson;
        }catch (Exception e){
            return "Error creating user: " + e.getLocalizedMessage();
        }
    }
}
