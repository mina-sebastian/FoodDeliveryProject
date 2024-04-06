package me.mina.fooddeli;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.command.HelpCommand;
import me.mina.fooddeli.command.MyOrderInfoCommand;
import me.mina.fooddeli.command.create.*;
import me.mina.fooddeli.command.show.*;
import me.mina.fooddeli.command.work.WorkCommand;
import me.mina.fooddeli.command.work.WorkDeliveryPersonCommand;
import me.mina.fooddeli.command.work.WorkRestaurantCommand;
import me.mina.fooddeli.daoservices.DeliveryPersonRepositoryService;
import me.mina.fooddeli.daoservices.OrderRepositoryService;
import me.mina.fooddeli.daoservices.RestaurantRepositoryService;
import me.mina.fooddeli.daoservices.UserRepositoryService;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.order.OrderItem;
import me.mina.fooddeli.model.order.OrderStatus;
import me.mina.fooddeli.model.restaurant.MenuItem;
import me.mina.fooddeli.model.restaurant.Restaurant;
import me.mina.fooddeli.model.person.PremiumUser;
import me.mina.fooddeli.model.person.User;
import me.mina.fooddeli.model.person.UserInfo;
import me.mina.fooddeli.model.review.Review;

import java.util.ArrayList;
import java.util.List;

public class FoodDeliveryService {

    private static DeliveryPersonRepositoryService deliveryPersonRepositoryService;
    private static OrderRepositoryService orderRepositoryService;
    private static RestaurantRepositoryService restaurantRepositoryService;
    private static UserRepositoryService userRepositoryService;

    private static List<Command> commands;
    private static boolean testPopulated = false;

    public static void init(){
        if(deliveryPersonRepositoryService == null){
            deliveryPersonRepositoryService = new DeliveryPersonRepositoryService();
        }
        if(orderRepositoryService == null){
            orderRepositoryService = new OrderRepositoryService();
        }
        if(restaurantRepositoryService == null){
            restaurantRepositoryService = new RestaurantRepositoryService();
        }
        if(userRepositoryService == null){
            userRepositoryService = new UserRepositoryService();
        }

        if(commands == null){
            initCommands();
        }

        if(!testPopulated){
            testPopulated = true;
            populateTestData();
         }
    }

    private static void initCommands(){
        commands = new ArrayList<>();
        HelpCommand helpCommand = new HelpCommand();

        MyOrderInfoCommand myOrderInfoCommand = new MyOrderInfoCommand(userRepositoryService, orderRepositoryService);

        ShowCommand showCommand = new ShowCommand();
        showCommand.addSubcommand(new ShowUserSubcommand(userRepositoryService));
        showCommand.addSubcommand(new ShowRestaurantsSubcommand(restaurantRepositoryService));
        showCommand.addSubcommand(new ShowOrderSubcommand(orderRepositoryService));
        showCommand.addSubcommand(new ShowDeliveryPersonSubcommand(deliveryPersonRepositoryService));
        showCommand.addSubcommand(new ShowReviewsSubcommand(restaurantRepositoryService,
                deliveryPersonRepositoryService));

        CreateCommand createCommand = new CreateCommand();
        createCommand.addSubcommand(new CreateUserSubcommand(userRepositoryService));
        createCommand.addSubcommand(new CreateRestaurantSubcommand(restaurantRepositoryService));
        createCommand.addSubcommand(new CreateOrderSubcommand(restaurantRepositoryService,
                orderRepositoryService,
                userRepositoryService));
        createCommand.addSubcommand(new CreateDeliveryPersonSubcommand(deliveryPersonRepositoryService));
        createCommand.addSubcommand(new CreateReviewSubcommand(restaurantRepositoryService,
                userRepositoryService,
                deliveryPersonRepositoryService));

        WorkCommand workCommand = new WorkCommand();
        workCommand.addSubcommand(new WorkRestaurantCommand(restaurantRepositoryService,
                deliveryPersonRepositoryService,
                orderRepositoryService));
        workCommand.addSubcommand(new WorkDeliveryPersonCommand(deliveryPersonRepositoryService,
                orderRepositoryService));

        commands.add(helpCommand);
        commands.add(myOrderInfoCommand);
        commands.add(showCommand);
        commands.add(createCommand);
        commands.add(workCommand);
    }

    private static void populateTestData(){
        User u1 = new User("AlexSmith", "AlexPass123", new UserInfo("123 Maple Street, Springfield", "555-0101"));
        User u2 = new User("JenDoe", "JenSecure!89", new UserInfo("456 Oak Lane, Gotham City", "555-0202"));
        User u3 = new User("SamJohnson", "SammyJ2024#", new UserInfo("789 Pine Road, Metropolis", "555-0303"));
        User u4 = new PremiumUser("LizBrown", "LizB*Pass78", new UserInfo("321 Birch Ave, Star City", "555-0404"), 15.6);
        User u5 = new PremiumUser("MikeDavis", "MDavis$432", new UserInfo("654 Elm Street, Central City", "555-0505"), 70.88);

        MenuItem m1 = new MenuItem("Chicken Rice", 10, List.of("Rice", "Chicken", "Soy Sauce"));
        MenuItem m2 = new MenuItem("Beef Stew", 15, List.of("Beef", "Potatoes", "Carrots", "Onions"));
        MenuItem m3 = new MenuItem("Vegetable Pasta", 12, List.of("Pasta", "Tomatoes", "Zucchini", "Bell Peppers"));
        MenuItem m4 = new MenuItem("Fish Tacos", 8, List.of("Fish", "Corn Tortillas", "Cabbage", "Lime"));
        MenuItem m5 = new MenuItem("Mushroom Risotto", 14, List.of("Rice", "Mushrooms", "Parmesan", "Onions"));
        MenuItem m6 = new MenuItem("Pork Ramen", 13, List.of("Noodles", "Pork", "Eggs", "Green Onions", "Broth"));
        MenuItem m7 = new MenuItem("Caesar Salad", 9, List.of("Lettuce", "Croutons", "Parmesan", "Caesar Dressing"));
        MenuItem m8 = new MenuItem("BBQ Ribs", 18, List.of("Pork Ribs", "BBQ Sauce", "Coleslaw"));
        MenuItem m9 = new MenuItem("Quiche Lorraine", 11, List.of("Eggs", "Bacon", "Cheese", "Pie Crust"));
        MenuItem m10 = new MenuItem("Tom Yum Soup", 10, List.of("Broth", "Shrimp", "Mushrooms", "Lemongrass", "Chili Peppers"));

        Restaurant r1 = new Restaurant("La Rosa", List.of(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10));

        Review r1Review1 = new Review(u1, "Great food and service!", 10);
        r1.addReview(r1Review1);


        MenuItem m11 = new MenuItem("Spaghetti Carbonara", 12, List.of("Pasta", "Eggs", "Bacon", "Parmesan"));
        MenuItem m12 = new MenuItem("Greek Salad", 9, List.of("Tomatoes", "Cucumbers", "Olives", "Feta Cheese"));
        MenuItem m13 = new MenuItem("Lamb Curry", 16, List.of("Lamb", "Curry Powder", "Coconut Milk", "Rice"));
        MenuItem m14 = new MenuItem("Margherita Pizza", 10, List.of("Dough", "Tomato Sauce", "Mozzarella", "Basil"));
        MenuItem m15 = new MenuItem("Duck Confit", 20, List.of("Duck", "Potatoes", "Garlic", "Thyme"));

        Restaurant r2 = new Restaurant("Bistro Gourmet", List.of(m11, m12, m13, m14, m15));

        userRepositoryService.create(u1);
        userRepositoryService.create(u2);
        userRepositoryService.create(u3);
        userRepositoryService.create(u4);
        userRepositoryService.create(u5);

        Review r2Review1 = new Review(u1, "Great food and service!", 9);
        r2.addReview(r2Review1);
        Review r2Review2 = new Review(u2, "The chicken rice was meh!", 6);
        r2.addReview(r2Review2);
        Review r2Review3 = new Review(u3, "The beef stew was amazing!", 10);
        r2.addReview(r2Review3);

        restaurantRepositoryService.create(r1);
        restaurantRepositoryService.create(r2);


        Order o1 = new Order(u1.getUserInfo(), List.of(new OrderItem(m5, 2), new OrderItem(m8, 1), new OrderItem(m9, 10)), OrderStatus.PENDING);
        Order o2 = new Order(u5.getUserInfo(), List.of(new OrderItem(m11, 1), new OrderItem(m15, 3)), OrderStatus.PENDING);

        orderRepositoryService.create(o1, r1);
        orderRepositoryService.create(o2, r2);
    }

    public static String getAllCommands(){
        StringBuilder sb = new StringBuilder();
        sb.append("Commands: \n");
        commands.forEach(c -> sb.append("- ").append(c.getCommand()).append(": ").append(c.getPossibleSubcommands()).append("\n"));
        return sb.toString();
    }

    public static List<Command> getCommands() {
        return commands;
    }

    public static void executeCommand(String command){
        Command cmd = commands.stream()
                .filter(c -> c.getCommand().equalsIgnoreCase(command))
                .findFirst()
                .orElse(null);
        if(cmd != null){
            String s = cmd.execute();
            System.out.println(s);
        }else{
            System.out.println("Invalid command");
        }
    }

    public static DeliveryPersonRepositoryService getDeliveryPersonRepositoryService() {
        return deliveryPersonRepositoryService;
    }

    public static OrderRepositoryService getOrderRepositoryService() {
        return orderRepositoryService;
    }

    public static RestaurantRepositoryService getRestaurantRepositoryService() {
        return restaurantRepositoryService;
    }

    public static UserRepositoryService getUserRepositoryService() {
        return userRepositoryService;
    }
}