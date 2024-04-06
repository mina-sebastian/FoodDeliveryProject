package me.mina.fooddeli.command.show;

import me.mina.fooddeli.command.Command;

public class ShowCommand extends Command {


    public ShowCommand() {
        super("show");
    }

    @Override
    public String execute() {
        System.out.println("Choose a subcommand: " + getPossibleSubcommands());
        String cmd = getScanner().nextLine();
        return executeSubcommand(cmd);
    }
}
