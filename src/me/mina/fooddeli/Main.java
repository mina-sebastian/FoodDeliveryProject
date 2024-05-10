package me.mina.fooddeli;

import me.mina.fooddeli.utils.DatabaseConnection;

import java.util.Scanner;

public class Main {
    private static Main instance;
    private static Scanner scanner;

    public static void main(String[] args) {
        // Connect to the database
        try{
            DatabaseConnection.getInstance();
            System.out.println("Connected to the database successfully");
        } catch (Exception e){
            System.out.println("Error connecting to the database");
            return;
        }

        scanner = new Scanner(System.in);
        FoodDeliveryService.init();
        System.out.println(FoodDeliveryService.getAllCommands());

        while (true) {
            System.out.println("Enter a command:");
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                break;
            }
            System.out.println("You entered: " + command);
            FoodDeliveryService.executeCommand(command);
        }
    }

    public Scanner getScanner() {
        return scanner;
    }

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

}
