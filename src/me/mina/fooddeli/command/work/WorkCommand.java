package me.mina.fooddeli.command.work;

import me.mina.fooddeli.command.Command;

public class WorkCommand extends Command {

    public WorkCommand() {
        super("work");
    }
    @Override
    public String execute() {
        return choosePrompt();
    }
}
