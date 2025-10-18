package olms;

import java.util.*;
import java.sql.*;

public class adminMenu {
    private int adminId;

    public adminMenu(int adminId) {
        this.adminId = adminId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    Scanner sc = new Scanner(System.in);

    public void viewAllEmployees() {
        String query = "SELECT * FROM users WHERE role='employee'";
        try (Connection conn = database.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n===== Employee List =====");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Username: " + rs.getString("username") +
                        ", Department: " + rs.getString("department") +
                        ", Email: " + rs.getString("email") +
                        ", Phone: " + rs.getString("phone"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching employees: " + e.getMessage());
        }
    }

    // ➕ Add Employee
    public void addEmployee() {
        String name = helper.getString(sc, "Enter employee name: ");
        String username = helper.getString(sc, "Enter username: ");
        String password = helper.getString(sc, "Enter password: ");
        String department = helper.getString(sc, "Enter department: ");
        String email = helper.getString(sc, "Enter email: ");
        String phone = helper.getString(sc, "Enter phone: ");

        String query = "INSERT INTO users (name, username, password, department, email, phone, role, created_at) VALUES (?, ?, ?, ?, ?, ?, 'employee', NOW())";
        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, department);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.executeUpdate();

            System.out.println("Employee added successfully!");
            helper.logActivity(adminId, "Added employee: " + name);

        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    // 🗑 Delete Employee
    public void deleteEmployee() {
        viewAllEmployees();
        String idStr = helper.getString(sc, "Enter Employee ID to delete: ");
        try {
            int id = Integer.parseInt(idStr);
            String query = "DELETE FROM users WHERE id = ? AND role='employee'";
            try (Connection conn = database.getConnection();
                    PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows > 0)
                    System.out.println("Employee deleted successfully!");
                else
                    System.out.println("Employee not found or not a user.");
                helper.logActivity(adminId, "Deleted employee with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }

    public void viewAllLeaves() {
        String query = "SELECT l.id, u.name, l.start_date, l.end_date, l.reason, l.status FROM leaves l JOIN users u ON l.employee_id = u.id";
        try (Connection conn = database.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n===== All Leave Requests =====");
            while (rs.next()) {
                System.out.println("Leave ID: " + rs.getInt("id") +
                        ", Employee: " + rs.getString("name") +
                        ", From: " + rs.getString("start_date") +
                        ", To: " + rs.getString("end_date") +
                        ", Reason: " + rs.getString("reason") +
                        ", Status: " + rs.getString("status"));
            }

        } catch (SQLException e) {
            System.out.println("Error viewing leaves: " + e.getMessage());
        }
    }

    public void handleLeaveApproval() {
        viewAllLeaves();
        String idStr = helper.getString(sc, "Enter Leave ID: ");
        int leaveId = Integer.parseInt(idStr);
        int action = helper.getIntInput(sc, "Enter action (1: Approve / 2: Reject): ");

        String status = action == 1 ? "Approved"
                : action == 2 ? "Rejected" : null;

        String query = "UPDATE leaves SET status = ? WHERE id = ?";
        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, leaveId);
            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Leave " + status + " successfully!");
            else
                System.out.println("Leave ID not found.");
            helper.logActivity(adminId, status + " leave with ID: " + leaveId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (SQLException e) {
            System.out.println("Error updating leave status: " + e.getMessage());
        }

    }

    // 👑 Promote User to Admin
    public void promoteUserToAdmin() {
        viewAllEmployees();
        String idStr = helper.getString(sc, "Enter User ID to promote: ");
        try {
            int id = Integer.parseInt(idStr);
            String query = "UPDATE users SET role='admin' WHERE id = ?";
            try (Connection conn = database.getConnection();
                    PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows > 0)
                    System.out.println("User promoted to admin!");
                else
                    System.out.println("User ID not found.");
            }
            helper.logActivity(adminId, "Promoted user with ID: " + id + " to admin");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (SQLException e) {
            System.out.println("Error promoting user: " + e.getMessage());
        }
    }
}

class admin {
    public static void showMenu(int adminId) {
        Scanner sc = new Scanner(System.in);
        adminMenu adminMenu = new adminMenu(adminId);

        while (true) {
            System.out.println("\n===== Admin Dashboard =====");
            System.out.println("1. View All Employees");
            System.out.println("2. Add Employee");
            System.out.println("3. Delete Employee");
            System.out.println("4. View All Leave Requests");
            System.out.println("5. Approve/Reject Leave");
            System.out.println("6. Promote User to Admin");
            System.out.println("7. Logout");
            String choice = helper.getString(sc, "Choose an option: ");
            switch (choice) {
                case "1":
                    adminMenu.viewAllEmployees();
                    break;
                case "2":
                    adminMenu.addEmployee();
                    break;
                case "3":
                    adminMenu.deleteEmployee();
                    break;
                case "4":
                    adminMenu.viewAllLeaves();
                    break;
                case "5":
                    adminMenu.handleLeaveApproval();
                    break;
                case "6":
                    adminMenu.promoteUserToAdmin();
                    break;
                case "7":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
