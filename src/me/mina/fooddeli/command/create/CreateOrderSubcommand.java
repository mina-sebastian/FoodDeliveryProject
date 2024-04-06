package me.mina.fooddeli.command.create;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.OrderRepositoryService;
import me.mina.fooddeli.daoservices.RestaurantRepositoryService;
import me.mina.fooddeli.daoservices.UserRepositoryService;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.order.OrderItem;
import me.mina.fooddeli.model.order.OrderStatus;
import me.mina.fooddeli.model.restaurant.MenuItem;
import me.mina.fooddeli.model.restaurant.Restaurant;
import me.mina.fooddeli.model.person.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreateOrderSubcommand extends Command {
    private RestaurantRepositoryService restaurantRepositoryService;
    private OrderRepositoryService orderRepositoryService;
    private UserRepositoryService userRepositoryService;

    public CreateOrderSubcommand(RestaurantRepositoryService restaurantRepositoryService,
                                 OrderRepositoryService orderRepositoryService,
                                 UserRepositoryService userRepositoryService) {
        super("order");

        this.restaurantRepositoryService = restaurantRepositoryService;
        this.orderRepositoryService = orderRepositoryService;
        this.userRepositoryService = userRepositoryService;
    }

    @Override
    public String execute() {
        System.out.println("Creating order...");
        String availableRestaurants = restaurantRepositoryService.getAll().stream()
                .map(Restaurant::getName)
                .reduce("", (acc, restaurantName) -> acc + restaurantName + ", ");
        System.out.println("Available restaurants: " + availableRestaurants);
        System.out.println("Chose restaurant: ");
        String restaurantName = getScanner().nextLine();
        Optional<Restaurant> restaurantOptional = restaurantRepositoryService
                                                    .findRestaurantByName(restaurantName);
        if(restaurantOptional.isEmpty()) {
            return "Restaurant not found";
        }

        System.out.println("Enter user name: ");
        String userName = getScanner().nextLine();
        Optional<User> userOptional = userRepositoryService.findUserByName(userName);

        if(userOptional.isEmpty()) {
            return "User not found";
        }

        String menuItems = String.join(", ", restaurantOptional.get().getMenu().stream().map(MenuItem::getName).toList());

        System.out.println("Restaurant menu: "+ menuItems);

        System.out.println("Enter order items length: ");
        int orderItemsLength = getScanner().nextInt();
        getScanner().nextLine();


        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < orderItemsLength; i++) {
            System.out.println("Enter item " + (i+1) + " name: ");
            String itemName = getScanner().nextLine();

            Optional<MenuItem> menuItemOptional = restaurantOptional.get().getMenu().stream()
                    .filter(menuItem -> menuItem.getName().equals(itemName))
                    .findFirst();

            if(menuItemOptional.isEmpty()) {
                return "Item not found in menu";
            }

            System.out.println("Enter item " + (i+1) + "(" + itemName + ") quantity: ");
            int quantity = getScanner().nextInt();
            getScanner().nextLine();

            OrderItem orderItem = new OrderItem(menuItemOptional.get(), quantity);
            orderItems.add(orderItem);
        }


        Order order = new Order(userOptional.get().getUserInfo());
        order.setItems(orderItems);
        order.setStatus(OrderStatus.PENDING);

        orderRepositoryService.create(order, restaurantOptional.get());
        return "Order created: " + order;
    }
}
