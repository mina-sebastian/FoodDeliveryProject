package me.mina.fooddeli.command;

import me.mina.fooddeli.FoodDeliveryService;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public String execute() {
        return FoodDeliveryService.getAllCommands();
    }
}
