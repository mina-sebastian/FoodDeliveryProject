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

public class WorkRestaurantCommand extends Command {

    private RestaurantRepositoryService restaurantRepositoryService;
    private DeliveryPersonRepositoryService deliveryPersonRepositoryService;
    private OrderRepositoryService orderRepositoryService;

    public WorkRestaurantCommand(RestaurantRepositoryService restaurantRepositoryService,
                                 DeliveryPersonRepositoryService deliveryPersonRepositoryService,
                                 OrderRepositoryService orderRepositoryService) {
        super("restaurant");

        this.restaurantRepositoryService = restaurantRepositoryService;
        this.deliveryPersonRepositoryService = deliveryPersonRepositoryService;
        this.orderRepositoryService = orderRepositoryService;
    }

    //This method updates the restaurant to randomly complete an order from the queue
    @Override
    public String execute() {
        System.out.println("Working on restaurant...");
        String availableRestaurants = restaurantRepositoryService.getAll().stream()
                .map(Restaurant::getName)
                .reduce("", (acc, restaurantName) -> acc + restaurantName + ", ");
        System.out.println("Available restaurants: " + availableRestaurants);
        System.out.println("Enter the restaurant name: ");
        String restaurantName = getScanner().nextLine();
        Optional<Restaurant> restaurantOptional = restaurantRepositoryService.findRestaurantByName(restaurantName);

        if(restaurantOptional.isEmpty()) {
            return "Restaurant not found";
        }

        Restaurant restaurant = restaurantOptional.get();

        Order order = restaurant.getQueue().peek();

        if(order == null) {
            return "No orders in the queue";
        }

        // Randomly change the current order
        if(Math.random() > 0.1) {
            if(Math.random() > 0.4) {
                switch (order.getStatus()) {
                    case PENDING -> order.setStatus(OrderStatus.CONFIRMED);
                    case CONFIRMED -> order.setStatus(OrderStatus.PREPARING);
                    case PREPARING -> order.setStatus(OrderStatus.READY);
                }
                orderRepositoryService.update(order.getId(), order);
                if(order.getStatus() == OrderStatus.READY){
                    Optional<DeliveryPerson> deliveryPerson = deliveryPersonRepositoryService.findFirstAvailable();
                    if(deliveryPerson.isPresent()){
                        restaurant.getQueue().poll();

                        deliveryPerson.get().setCurrentOrder(order);


                        restaurantRepositoryService.update(restaurant.getId(), restaurant);
                        deliveryPersonRepositoryService.update(deliveryPerson.get().getId(), deliveryPerson.get());
                        return "Order is ready and delivery person is assigned.\nOrder info: " + order;
                    }else{
                        return "Order is ready but there is no delivery person available.";
                    }
                }
                return "Order changed to "+ order.getStatus() +".\nOrder info: " + order;

            }else{
                return "Order not changed.\nOrder info: " + order;
            }
        } else {
            order.setStatus(OrderStatus.CANCELLED);
            restaurant.getQueue().poll();

            orderRepositoryService.update(order.getId(), order);
            restaurantRepositoryService.update(restaurant.getId(), restaurant);
            return "Order was cancelled by the user";
        }
    }
}
