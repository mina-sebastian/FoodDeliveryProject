package me.mina.fooddeli.command.show;

import me.mina.fooddeli.command.Command;
import me.mina.fooddeli.daoservices.UserRepositoryService;
import me.mina.fooddeli.utils.Utils;

public class ShowUserSubcommand extends Command {

    private UserRepositoryService userRepositoryService;

    public ShowUserSubcommand(UserRepositoryService userRepositoryService) {
        super("user");

        this.userRepositoryService = userRepositoryService;
    }

    @Override
    public String execute() {
        System.out.println("Showing users...");
        System.out.println("Enter type of user (all/regular/premium): ");
        String choice = getScanner().nextLine();
        return switch (choice) {
            case "all" -> Utils.objectListToString(userRepositoryService.getAll());
            case "regular" -> Utils.objectListToString(userRepositoryService.getAllRegularUsers());
            case "premium" -> Utils.objectListToString(userRepositoryService.getAllPremiumUsers());
            default -> "Invalid user type";
        };
    }
}
