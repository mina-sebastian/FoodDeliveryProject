package me.mina.fooddeli.command.create;

import me.mina.fooddeli.command.Command;

public class CreateCommand extends Command {
    public CreateCommand() {
        super("create");
    }

    @Override
    public String execute() {
        return choosePrompt();
    }
}
