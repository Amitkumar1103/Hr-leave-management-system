package olms;

import java.util.*;
import java.sql.*;
import java.time.*;

class login {
    private int id;
    private String username;
    private String password;
    private String role;
    private String created_at;

    public login(int id, String username, String password, String role, String created_at) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getCreatedAt() {
        return created_at;
    }
}

class loginManager {

    public login login(String username, String password) {
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new login(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("created_at"));
            } else {
                System.out.println("Invalid username or password.");
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }

    public login signup(String Name, String username, String password, String department, String email, String phone) {
        if (Name == null || Name.trim().isEmpty() || username == null || username.trim().isEmpty() || password == null
                || password.trim().isEmpty() || department == null || department.trim().isEmpty() || email == null
                || email.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            System.out.println("Name, username and password cannot be empty.");
            return null;
        }
        String createdAt = LocalDate.now().atTime(LocalTime.now()).toString();
        String query = "INSERT INTO users (name, username, password, department, email, phone, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, Name);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, department);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.setString(7, createdAt);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return new login(id, username, password, "employee", createdAt);
                }
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry"))
                System.out.println("Username already exists!");
            else
                System.out.println("Signup error: " + e.getMessage());
        }
        return null;
    }
}

class EmployeeApp {

    public static void main(String[] args) {
        Connection conn = database.getConnection();
        if (conn != null) {
            System.out.println("Connected to MySQL successfully!");
        } else {
            System.out.println("Connection failed.");
        }
        Scanner sc = new Scanner(System.in);
        loginManager loginManager = new loginManager();
        login currentUser = null;
        while (true) {
            System.out.println("\n===== Welcome to EMS =====");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Exit");
            int choice = helper.getIntInput(sc, "Enter choice: ");
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice, please select between 1-3.");
                continue;
            }

            switch (choice) {
                case 1:
                    String username = helper.getString(sc, "Enter username: ");
                    String password = helper.getString(sc, "Enter password: ");
                    currentUser = loginManager.login(username, password);
                    if (currentUser != null) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Login failed, try again.");
                        continue;
                    }
                    break;
                case 2:
                    String Name = helper.getString(sc, "Enter Name: ");
                    String username1 = helper.getString(sc, "Enter username: ");
                    String password1 = helper.getString(sc, "Enter password: ");
                    String department = helper.getString(sc, "Enter department: ");
                    String email = helper.getString(sc, "Enter email: ");
                    String phone = helper.getString(sc, "Enter phone number: ");
                    currentUser = loginManager.signup(Name, username1, password1, department, email, phone);
                    if (currentUser != null) {
                        System.out.println("Signup successful!. You can now login with your credentials");
                    } else {
                        System.out.println("Signup failed, try again.");
                        continue;
                    }
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
            System.out.println("\nLogged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");

            if (currentUser.getRole().equalsIgnoreCase("employee")) {
                Employee.showMenu(currentUser.getId());
            } else {
                admin.showMenu(currentUser.getId());
            }

        }
    }
}