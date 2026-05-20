import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Entry point - menu-driven console interface for User Management System.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   User Account Management System");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> register();
                    case 2 -> login();
                    case 3 -> updateProfile();
                    case 4 -> deleteUser();
                    case 5 -> viewAllUsers();
                    case 6 -> validatePasswordDemo();
                    case 0 -> {
                        running = false;
                        System.out.println("Thank you. Goodbye!");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Register (Signup)");
        System.out.println("2. Login");
        System.out.println("3. Update User Profile");
        System.out.println("4. Delete User");
        System.out.println("5. View All Users (Admin)");
        System.out.println("6. Test Password Validation");
        System.out.println("0. Exit");
    }

    private static void register() throws SQLException {
        System.out.println("\n--- User Registration ---");
        String name = readLine("Name: ");
        String email = readLine("Email: ");
        String password = readLine("Password: ");

        User user = userService.registerUser(name, email, password);
        System.out.println("Registration successful! " + user);
    }

    private static void login() throws SQLException {
        System.out.println("\n--- User Login ---");
        String email = readLine("Email: ");
        String password = readLine("Password: ");

        Optional<User> user = userService.loginUser(email, password);
        if (user.isPresent()) {
            System.out.println("Login successful! Welcome, " + user.get().getName());
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private static void updateProfile() throws SQLException {
        System.out.println("\n--- Update Profile ---");
        int id = readInt("User ID: ");
        String name = readLine("New name: ");
        String email = readLine("New email: ");
        String newPassword = readLine("New password (leave blank to keep current): ");

        boolean updated = userService.updateUser(
                id, name, email, newPassword.isBlank() ? null : newPassword);
        System.out.println(updated ? "Profile updated successfully." : "Update failed.");
    }

    private static void deleteUser() throws SQLException {
        System.out.println("\n--- Delete User ---");
        int id = readInt("User ID to delete: ");
        String confirm = readLine("Are you sure? (yes/no): ");
        if ("yes".equalsIgnoreCase(confirm.trim())) {
            boolean deleted = userService.deleteUser(id);
            System.out.println(deleted ? "User deleted successfully." : "Delete failed.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void viewAllUsers() throws SQLException {
        System.out.println("\n--- All Users (Admin) ---");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        System.out.printf("%-5s %-20s %-30s%n", "ID", "Name", "Email");
        System.out.println("-".repeat(60));
        for (User u : users) {
            System.out.printf("%-5d %-20s %-30s%n", u.getId(), u.getName(), u.getEmail());
        }
        System.out.println("Total users: " + users.size());
    }

    private static void validatePasswordDemo() {
        System.out.println("\n--- Password Validation ---");
        String password = readLine("Enter password to validate: ");
        if (userService.isValidPassword(password)) {
            System.out.println("Password is STRONG and valid.");
        } else {
            System.out.println("Password is WEAK or invalid.");
            System.out.println("Rules: min 8 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special (@#$%^&+=!)");
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
