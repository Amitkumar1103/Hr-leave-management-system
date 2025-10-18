package olms;

import java.util.*;
import java.sql.*;

public class helper {
    public static int getIntInput(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a number.");
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static String getString(Scanner sc, String prompt) {
        String string;
        while (true) {
            System.out.print(prompt);
            try {
                string = sc.nextLine();
                return string;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a valid string.");
                sc.nextLine();
            }
            // } catch (NullPointerException e) {
            // System.out.println("Input cannot be null, please enter a valid string.");
            // sc.nextLine();
            // }
        }
    }

    public static void logActivity(int userId, String action) {
        String query = "INSERT INTO activity_log (user_id, activity, created_at) VALUES (?, ?, NOW())";
        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setString(2, action);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error logging activity: " + e.getMessage());
        }
    }
}
