package me.mina.fooddeli.command;

import me.mina.fooddeli.daoservices.OrderRepositoryService;
import me.mina.fooddeli.daoservices.UserRepositoryService;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.person.PremiumUser;
import me.mina.fooddeli.model.person.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyOrderInfoCommand extends Command {
    private UserRepositoryService userRepositoryService;
    private OrderRepositoryService orderRepositoryService;
    public MyOrderInfoCommand(UserRepositoryService userRepositoryService, OrderRepositoryService orderRepositoryService) {
        super("myorderinfo");

        this.userRepositoryService = userRepositoryService;
        this.orderRepositoryService = orderRepositoryService;
    }

    @Override
    public String execute() {
        System.out.println("My Order Info");
        System.out.println("Enter your name: ");
        String name = getScanner().nextLine();
        System.out.println("Enter your password: ");
        String password = getScanner().nextLine();

        Optional<User> userOptional = userRepositoryService.getUserByCreds(name, password);

        if(userOptional.isEmpty()) {
            return "User not found. Check your credentials.";
        }

        User user = userOptional.get();

        List<Order> orders = orderRepositoryService.getOrdersByPhoneNumber(user.getUserInfo().getPhoneNumber());

        if(orders.isEmpty()) {
            return "No orders found for you.";
        }

        StringBuilder ordersInfo = new StringBuilder();
        for(int i = 0; i < orders.size(); i++) {
            double price = orders.get(i).getItems().stream()
                    .mapToDouble(orderItem -> orderItem.getItem().getPrice() * orderItem.getQuantity())
                    .sum();
            ordersInfo.append(i + 1).append(". ");
            if(user instanceof PremiumUser) {
                ordersInfo.append("PREMIUM ");
                price -= (price/100) * ((PremiumUser) user).getDiscountRate();
            }

            ordersInfo.append(price).append(" ").append(orders.get(i)).append("\n");
        }

        return ordersInfo.toString();
    }
}
