package olms;

import java.util.Scanner;
import java.sql.*;

public class EmployeeMenu {
    private int empId;

    public EmployeeMenu(int empId) {
        this.empId = empId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    Scanner sc = new Scanner(System.in);

    public void applyLeave() {
        String startDate = helper.getString(sc, "Enter leave start date (YYYY-MM-DD): ");
        String endDate = helper.getString(sc, "Enter leave end date (YYYY-MM-DD): ");
        String reason = helper.getString(sc, "Enter reason: ");

        String query = "INSERT INTO leaves (employee_id, start_date, end_date, reason, status) VALUES (?, ?, ?, ?, 'Pending')";

        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, empId);
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            ps.setString(4, reason);
            ps.executeUpdate();
            System.out.println("Leave request submitted successfully!");
            helper.logActivity(empId, "Applied for leave from " + startDate + " to " + endDate);
        } catch (SQLException e) {
            System.out.println("Error applying leave: " + e.getMessage());
        }
    }

    public void viewLeaveHistory() {
        String query = "SELECT * FROM leaves WHERE employee_id = ?";
        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== My Leave History =====");
            while (rs.next()) {
                System.out.println("Leave ID: " + rs.getInt("id") +
                        ", From: " + rs.getString("start_date") +
                        ", To: " + rs.getString("end_date") +
                        ", Reason: " + rs.getString("reason") +
                        ", Status: " + rs.getString("status"));
            }
            helper.logActivity(empId, "Viewed leave history");
        } catch (SQLException e) {
            System.out.println("Error viewing leave history: " + e.getMessage());
        }
    }

    public void viewProfile() {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n===== My Profile =====");
                System.out.println("Employee ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Department: " + rs.getString("department"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Created At: " + rs.getString("created_at"));
            } else {
                System.out.println("Profile not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing profile: " + e.getMessage());
        }
    }
}

class Employee {
    public static void showMenu(int empId) {
        Scanner sc = new Scanner(System.in);
        EmployeeMenu empMenu = new EmployeeMenu(empId);

        while (true) {
            System.out.println("\n===== Employee Dashboard =====");
            System.out.println("1. Apply for Leave");
            System.out.println("2. View My Leave History");
            System.out.println("3. View My Profile");
            System.out.println("4. Logout");
            int choice = helper.getIntInput(sc, "Enter your choice: ");
            switch (choice) {
                case 1:
                    empMenu.applyLeave();
                    break;
                case 2:
                    empMenu.viewLeaveHistory();
                    break;
                case 3:
                    empMenu.viewProfile();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice, please select between 1–4.");
            }
        }
    }
}