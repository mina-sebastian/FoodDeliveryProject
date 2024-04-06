package me.mina.fooddeli.command.create;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.UserRepositoryService;
import me.mina.fooddeli.model.person.PremiumUser;
import me.mina.fooddeli.model.person.User;
import me.mina.fooddeli.model.person.UserInfo;

public class CreateUserSubcommand extends Command {

    UserRepositoryService userRepositoryService;

    public CreateUserSubcommand(UserRepositoryService userRepositoryService) {
        super("users");
        this.userRepositoryService = userRepositoryService;
    }

    @Override
    public String execute() {
        try{
            System.out.println("Creating user...");
            System.out.println("Enter username: ");
            String username = getScanner().nextLine();
            System.out.println("Enter password: ");
            String password = getScanner().nextLine();
            System.out.println("Enter address: ");
            String address = getScanner().nextLine();
            System.out.println("Enter phone number: ");
            String phone = getScanner().nextLine();
            System.out.println("Is premium? (true/false): ");
            boolean premium = getScanner().nextBoolean();


            User user;
            if(premium){
                System.out.println("Enter discount rate: ");
                double discountRate = getScanner().nextDouble();
                user = new PremiumUser(username, password, new UserInfo(address, phone), discountRate);
            }else{
                user = new User(username, password, new UserInfo(address, phone));
            }

            userRepositoryService.create(user);

            return "User created: " + user;
        }catch (Exception e){
            return "Error creating user: " + e.getLocalizedMessage();
        }
    }
}
