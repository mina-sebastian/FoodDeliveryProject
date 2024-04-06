package me.mina.fooddeli.command;

import me.mina.fooddeli.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public abstract class Command {

    private List<Command> subcommands;

    private final String command;

    public Command(String command) {
        this.command = command;
        this.subcommands = new ArrayList<>();
    }

    public abstract String execute();

    public List<Command> getSubcommands() {
        return subcommands;
    }

    public void setSubcommands(List<Command> subcommands) {
        this.subcommands = subcommands;
    }

    public void addSubcommand(Command subcommand) {
        subcommands.add(subcommand);
    }

    public Scanner getScanner() {
        return Application.getInstance().getScanner();
    }

    public Command getSubcommand(String subcommand) {
        return subcommands.stream()
                .filter(sub -> sub.command.equalsIgnoreCase(subcommand))
                .findFirst()
                .orElse(null);
    }

    public String executeSubcommand(String subcommand) {
        Command command = getSubcommand(subcommand);
        if (command != null) {
            return command.execute();
        }
        return "Invalid subcommand";
    }

    public String getPossibleSubcommands() {
        return subcommands.stream()
                .map(Command::getCommand)
                .collect(Collectors.joining(", "));
    }

    public String getCommand() {
        return command;
    }

}
