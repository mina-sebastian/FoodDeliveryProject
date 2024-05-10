package me.mina.fooddeli;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.command.HelpCommand;
import me.mina.fooddeli.command.MyOrderInfoCommand;
import me.mina.fooddeli.command.create.*;
import me.mina.fooddeli.command.show.*;
import me.mina.fooddeli.command.work.WorkCommand;
import me.mina.fooddeli.command.work.WorkDeliveryPersonCommand;
import me.mina.fooddeli.command.work.WorkRestaurantCommand;
import me.mina.fooddeli.dao.ReviewDao;
import me.mina.fooddeli.daoservices.DeliveryPersonRepositoryService;
import me.mina.fooddeli.daoservices.OrderRepositoryService;
import me.mina.fooddeli.daoservices.RestaurantRepositoryService;
import me.mina.fooddeli.daoservices.UserRepositoryService;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.order.OrderItem;
import me.mina.fooddeli.model.order.OrderStatus;
import me.mina.fooddeli.model.person.DeliveryPerson;
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
    private static ReviewDao reviewDao;

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
        if(reviewDao == null){
            reviewDao = new ReviewDao();
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

    private static void populateTestData() {
        User u1 = new User(1, "AlexSmith", "AlexPass123", new UserInfo("123 Maple Street, Springfield", "555-0101"));
        User u2 = new User(2, "JenDoe", "JenSecure!89", new UserInfo("456 Oak Lane, Gotham City", "555-0202"));
        User u3 = new User(3, "SamJohnson", "SammyJ2024#", new UserInfo("789 Pine Road, Metropolis", "555-0303"));
        User u4 = new PremiumUser(4, "LizBrown", "LizB*Pass78", new UserInfo("321 Birch Ave, Star City", "555-0404"), 15.6);
        User u5 = new PremiumUser(5, "MikeDavis", "MDavis$432", new UserInfo("654 Elm Street, Central City", "555-0505"), 70.88);

        MenuItem m1 = new MenuItem(1, "Chicken Rice", 10, List.of("Rice", "Chicken", "Soy Sauce"));
        MenuItem m2 = new MenuItem(2, "Beef Stew", 15, List.of("Beef", "Potatoes", "Carrots", "Onions"));
        MenuItem m3 = new MenuItem(3, "Vegetable Pasta", 12, List.of("Pasta", "Tomatoes", "Zucchini", "Bell Peppers"));
        MenuItem m4 = new MenuItem(4, "Fish Tacos", 8, List.of("Fish", "Corn Tortillas", "Cabbage", "Lime"));
        MenuItem m5 = new MenuItem(5, "Mushroom Risotto", 14, List.of("Rice", "Mushrooms", "Parmesan", "Onions"));
        MenuItem m6 = new MenuItem(6, "Pork Ramen", 13, List.of("Noodles", "Pork", "Eggs", "Green Onions", "Broth"));
        MenuItem m7 = new MenuItem(7,"Caesar Salad", 9, List.of("Lettuce", "Croutons", "Parmesan", "Caesar Dressing"));
        MenuItem m8 = new MenuItem(8,"BBQ Ribs", 18, List.of("Pork Ribs", "BBQ Sauce", "Coleslaw"));
        MenuItem m9 = new MenuItem(9,"Quiche Lorraine", 11, List.of("Eggs", "Bacon", "Cheese", "Pie Crust"));
        MenuItem m10 = new MenuItem(10,"Tom Yum Soup", 10, List.of("Broth", "Shrimp", "Mushrooms", "Lemongrass", "Chili Peppers"));

        Restaurant r1 = new Restaurant(1, "La Rosa", List.of(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10));

        Review r1Review1 = new Review(1, u1, "Great food and service!", 10);
        r1.addReview(r1Review1);


        MenuItem m11 = new MenuItem(11, "Spaghetti Carbonara", 12, List.of("Pasta", "Eggs", "Bacon", "Parmesan"));
        MenuItem m12 = new MenuItem(12,"Greek Salad", 9, List.of("Tomatoes", "Cucumbers", "Olives", "Feta Cheese"));
        MenuItem m13 = new MenuItem(13,"Lamb Curry", 16, List.of("Lamb", "Curry Powder", "Coconut Milk", "Rice"));
        MenuItem m14 = new MenuItem(14,"Margherita Pizza", 10, List.of("Dough", "Tomato Sauce", "Mozzarella", "Basil"));
        MenuItem m15 = new MenuItem(15,"Duck Confit", 20, List.of("Duck", "Potatoes", "Garlic", "Thyme"));

        Restaurant r2 = new Restaurant(2, "Bistro Gourmet", List.of(m11, m12, m13, m14, m15));

        userRepositoryService.create(u1);
        userRepositoryService.create(u2);
        userRepositoryService.create(u3);
        userRepositoryService.create(u4);
        userRepositoryService.create(u5);

        Review r2Review1 = new Review(2, u1, "Great food and service!", 9);
        r2.addReview(r2Review1);
        Review r2Review2 = new Review(3, u2, "The chicken rice was meh!", 6);
        r2.addReview(r2Review2);
        Review r2Review3 = new Review(4, u3, "The beef stew was amazing!", 10);
        r2.addReview(r2Review3);

        restaurantRepositoryService.create(r1);
        restaurantRepositoryService.create(r2);

        Order o1 = new Order(1, u1.getUserInfo(), List.of(new OrderItem(m5, 2), new OrderItem(m8, 1), new OrderItem(m9, 10)), OrderStatus.PENDING);
        Order o2 = new Order(2, u5.getUserInfo(), List.of(new OrderItem(m11, 1), new OrderItem(m15, 3)), OrderStatus.PENDING);

        orderRepositoryService.create(o1, r1);
        orderRepositoryService.create(o2, r2);

        DeliveryPerson d1 = new DeliveryPerson(1, "Jerry", "iamjerry");
        DeliveryPerson d2 = new DeliveryPerson(2, "Tom", "iamtom");

        Review d1Review1 = new Review(5, u1, "Fast delivery!", 10);
        d1.addReview(d1Review1);
        Review d1Review2 = new Review(6, u2, "The delivery was late!", 4);
        d1.addReview(d1Review2);

        Review d2Review1 = new Review(7, u3, "The delivery person was very polite!", 9);
        d2.addReview(d2Review1);
        Review d2Review2 = new Review(8, u4, "The delivery person was nice!", 9);
        d2.addReview(d2Review2);


        deliveryPersonRepositoryService.create(d1);
        deliveryPersonRepositoryService.create(d2);
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

    public static ReviewDao getReviewDao() {
        return reviewDao;
    }
}