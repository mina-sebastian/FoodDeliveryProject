package me.mina.fooddeli.command.work;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.DeliveryPersonRepositoryService;
import me.mina.fooddeli.daoservices.OrderRepositoryService;
import me.mina.fooddeli.daoservices.RestaurantRepositoryService;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.order.OrderStatus;
import me.mina.fooddeli.model.person.DeliveryPerson;
import me.mina.fooddeli.model.restaurant.Restaurant;

import java.util.Optional;

public class WorkDeliveryPersonCommand extends Command {

    private DeliveryPersonRepositoryService deliveryPersonRepositoryService;
    private OrderRepositoryService orderRepositoryService;

    public WorkDeliveryPersonCommand(DeliveryPersonRepositoryService deliveryPersonRepositoryService,
                                     OrderRepositoryService orderRepositoryService) {
        super("delivery");

        this.deliveryPersonRepositoryService = deliveryPersonRepositoryService;
        this.orderRepositoryService = orderRepositoryService;
    }

    //This method updates the restaurant to randomly complete an order from the queue
    @Override
    public String execute() {
        System.out.println("Working on delivery...");
        String availableDeliveryPersons = deliveryPersonRepositoryService.getAll().stream()
                .map(DeliveryPerson::getName)
                .reduce("", (acc, restaurantName) -> acc + restaurantName + ", ");
        System.out.println("Available delivery persons: " + availableDeliveryPersons);
        System.out.println("Enter the delivery person name: ");
        String deliveryPersonName = getScanner().nextLine();
        Optional<DeliveryPerson> deliveryPersonOptional = deliveryPersonRepositoryService.findByUsername(deliveryPersonName);

        if(deliveryPersonOptional.isEmpty()) {
            return "Delivery person not found";
        }

        DeliveryPerson deliveryPerson = deliveryPersonOptional.get();

        Order order = deliveryPerson.getCurrentOrder();

        if(order == null) {
            return "No orders for " + deliveryPersonName;
        }

        // Randomly change the current order
        if(Math.random() > 0.1) {
            if(Math.random() > 0.4) {
                switch (order.getStatus()) {
                    case READY -> order.setStatus(OrderStatus.ON_THE_WAY);
                    case ON_THE_WAY -> order.setStatus(OrderStatus.DELIVERED);
                }
                orderRepositoryService.update(order.getId(), order);
                if(order.getStatus() == OrderStatus.DELIVERED) {
                    deliveryPerson.setCurrentOrder(null);
                    deliveryPersonRepositoryService.update(deliveryPerson.getId(), deliveryPerson);
                    return "Order delivered.\nOrder info: " + order;
                }
                return "Order changed to "+ order.getStatus() +".\nOrder info: " + order;
            }else{
                return "Order not changed.\nOrder info: " + order;
            }
        } else {
            order.setStatus(OrderStatus.CANCELLED);
            deliveryPerson.setCurrentOrder(null);

            orderRepositoryService.update(order.getId(), order);
            deliveryPersonRepositoryService.update(deliveryPerson.getId(), deliveryPerson);
            return "Order was cancelled by the user";
        }
    }
}
