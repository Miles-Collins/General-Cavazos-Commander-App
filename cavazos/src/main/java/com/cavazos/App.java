package com.cavazos;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import org.json.simple.JSONArray;

public class App {

    private static String[] commandArray;
    private static Stack<String> commandHistory = new Stack<>();
    private static Stack<String> redoStack = new Stack<>();

    public static void main(String[] args) {
        String fileName = "commands.json";

        // read commands
        JSONArray commandJSONArray = JSONFile.readArray(fileName);
        if (commandJSONArray == null) {
            System.err.println("Could not load commands from resources: " + fileName);
            return;
        }
        commandArray = getCommandArray(commandJSONArray);

        // Start the interactive menu
        startMenu();
    }

    // Start the interactive menu loop
    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\n===== General Cavazos Commander =====");

        while (running) {
            System.out.println("\nOptions:");
            System.out.println("1. list    - Display all commands");
            System.out.println("2. issue   - Issue a command");
            System.out.println("3. random  - Issue a random command");
            System.out.println("4. undo    - Undo last command");
            System.out.println("5. redo    - Redo last undone command");
            System.out.println("6. history - View command history");
            System.out.println("7. clear   - Clear command history");
            System.out.println("8. help    - Show help information");
            System.out.println("9. quit    - Exit the program");
            System.out.print("\nEnter command: ");

            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "list":
                case "1":
                    displayAllCommands();
                    break;
                case "issue":
                case "2":
                    issueCommand();
                    break;
                case "random":
                case "3":
                    issueRandomCommand();
                    break;
                case "undo":
                case "4":
                    undoCommand();
                    break;
                case "redo":
                case "5":
                    redoCommand();
                    break;
                case "history":
                case "6":
                    displayCommandHistory();
                    break;
                case "clear":
                case "7":
                    clearCommandHistory();
                    break;
                case "help":
                case "8":
                    showHelp();
                    break;
                case "quit":
                case "9":
                    System.out.println("Exiting General Cavazos Commander. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Unknown command. Please try again.");
            }
        }

        scanner.close();
    }

    // Issue a command from the list
    public static void issueCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n----- Select a Command to Issue -----");
        print(commandArray);

        System.out.print("Enter command number (0-" + (commandArray.length - 1) + "): ");
        String input = scanner.nextLine().trim();

        try {
            int index = Integer.parseInt(input);
            if (index >= 0 && index < commandArray.length) {
                String command = commandArray[index];
                commandHistory.push(command);
                redoStack.clear();  // Clear redo stack when new command is issued
                System.out.println(">> Command issued: " + command);
            } else {
                System.out.println("Invalid command number. Please select a number between 0 and "
                    + (commandArray.length - 1));
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    // Issue a random command
    public static void issueRandomCommand() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(commandArray.length);
        String command = commandArray[randomIndex];
        commandHistory.push(command);
        redoStack.clear();  // Clear redo stack when new command is issued
        System.out.println(">> Random command issued: " + command);
    }

    // Display all available commands
    public static void displayAllCommands() {
        System.out.println("\n----- All Available Commands -----");
        print(commandArray);
    }

    // Undo the last issued command
    public static void undoCommand() {
        if (commandHistory.isEmpty()) {
            System.out.println("No commands to undo.");
        } else {
            String undoneCommand = commandHistory.pop();
            redoStack.push(undoneCommand);
            System.out.println(">> Undone: " + undoneCommand);
        }
    }

    // Redo the last undone command
    public static void redoCommand() {
        if (redoStack.isEmpty()) {
            System.out.println("No commands to redo.");
        } else {
            String redoneCommand = redoStack.pop();
            commandHistory.push(redoneCommand);
            System.out.println(">> Redone: " + redoneCommand);
        }
    }

    // Display the command history
    public static void displayCommandHistory() {
        System.out.println("\n----- Command History -----");
        if (commandHistory.isEmpty()) {
            System.out.println("No commands have been issued yet.");
        } else {
            System.out.println("Issued Commands (most recent last):");
            Object[] history = commandHistory.toArray();
            for (int i = 0; i < history.length; i++) {
                System.out.printf("%d. %s\n", i + 1, history[i]);
            }
        }

        System.out.println("\n----- Redo Stack -----");
        if (redoStack.isEmpty()) {
            System.out.println("No commands available to redo.");
        } else {
            System.out.println("Commands Available to Redo:");
            Object[] redo = redoStack.toArray();
            for (int i = 0; i < redo.length; i++) {
                System.out.printf("%d. %s\n", i + 1, redo[i]);
            }
        }
    }

    // Clear the command history
    public static void clearCommandHistory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to clear the command history? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(response) || "y".equals(response)) {
            commandHistory.clear();
            redoStack.clear();
            System.out.println("Command history cleared.");
        } else {
            System.out.println("Clear history cancelled.");
        }
    }

    // Show help information
    public static void showHelp() {
        System.out.println("\n===== General Cavazos Commander - Help =====");
        System.out.println("This application allows you to issue commands from General Cavazos.");
        System.out.println("\nAvailable Commands:");
        System.out.println("  list   - Displays all available military commands");
        System.out.println("  issue  - Allows you to select and issue a command");
        System.out.println("  random - Issues a randomly selected command");
        System.out.println("  undo   - Undoes the last issued command");
        System.out.println("  redo   - Redoes the last undone command");
        System.out.println("  history - Shows the command history and redo stack");
        System.out.println("  clear  - Clears all command history");
        System.out.println("  help   - Shows this help message");
        System.out.println("  quit   - Exits the application");
        System.out.println("\nUsage Tips:");
        System.out.println("  - You can use either the command name or its number");
        System.out.println("  - Undo and Redo work with a stack-based history system");
        System.out.println("  - When you issue a new command after undo, the redo stack is cleared");
    }

    public static void randomCommand(String[] commands, int numCommand) {
        Random rand = new Random();
        System.out.printf("Number\tCommand\n");
        System.out.printf("------\t---------------\n");
        for (int i = 0; i < numCommand; i++) {
            int randIndex = rand.nextInt(commands.length);
            System.out.printf("%04d\t%s\n", i, commands[randIndex]);
        }
    }

    // print command array
    public static void print(String[] commands) {
        System.out.printf("Number\tCommand\n");
        System.out.printf("------\t---------------\n");
        for (int i = 0; i < commands.length; i++) {
            System.out.printf("%04d\t%s\n", i, commands[i]);
        }
    }

    // get array of commands
    public static String[] getCommandArray(JSONArray commands) {
        String[] arr = new String[commands.size()];

        // get names from json object
        for (int i = 0; i < commands.size(); i++) {
            String command = commands.get(i).toString();
            arr[i] = command;
        }
        return arr;
    }
}
