package me.mina.fooddeli.command.show;

import me.mina.fooddeli.command.Command;

public class ShowCommand extends Command {


    public ShowCommand() {
        super("show");
    }

    @Override
    public String execute() {
        return choosePrompt();
    }
}
