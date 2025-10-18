# **📅 Hr Leave Management System (OLMS)**

A console-based **Online Leave Management System** built with **Core Java** principles, designed to manage employee leave requests, approvals, and system activity logging within an organization. This project showcases strong Object-Oriented Programming (OOP) practices, robust database interaction via JDBC, and a secure, role-based access structure.

## **✨ Project Overview**

This application provides a simple, yet powerful command-line interface for both system administrators and general employees. It ensures that all leave processes—from application to final approval—are centralized and tracked in a secure MySQL database.

## **🚀 Key Features**

| Feature                   | Role             | Description                                                                                           |
| :------------------------ | :--------------- | :---------------------------------------------------------------------------------------------------- |
| **Login / Signup System** | All Users        | Secure authentication based on username and password.                                                 |
| **Role-based Access**     | Admin / Employee | Segregates functionality: Employees can apply/view their leave; Admins can view/process all requests. |
| **Leave Management**      | Admin / Employee | Employees can apply for leave. Admins can **Approve** or **Reject** pending applications.             |
| **View All Leaves**       | Admin Only       | Comprehensive overview of all active and historical leave requests across the organization.           |
| **Data Validation**       | All Users        | Robust input checks and exception handling to maintain data integrity.                                |
| **Activity Logging**      | System           | Tracks critical user actions (e.g., login, leave application, approval) in a dedicated table.         |

## **🧠 Technology Stack**

| Category          | Technology              | Focus/Concepts Used                                                              |
| :---------------- | :---------------------- | :------------------------------------------------------------------------------- |
| **Core Language** | **Java (Core \+ JDBC)** | OOP (Encapsulation, Delegation), Dependency Injection, Data Typing.              |
| **Database**      | **MySQL**               | Relational data storage, efficient querying, and data persistence.               |
| **Architecture**  | **OOP Concepts**        | Separation of Concerns (Business Logic vs. UI/Menu), CRUD Operations.            |
| **Reliability**   | **Exception Handling**  | try-catch-finally blocks and SQL exception handling for robust application flow. |

## **🗂️ Database Schema**

The system relies on three primary tables:

| Table Name       | Purpose                                    | Key Fields                                                 |
| :--------------- | :----------------------------------------- | :--------------------------------------------------------- |
| **users**        | Stores all user accounts (Admin/Employee). | id, username, password, role, department                   |
| **leaves**       | Stores all employee leave applications.    | id, employee_id (FK), start_date, end_date, reason, status |
| **activity_log** | Records system actions for auditing.       | id, user_id (FK), activity, timestamp                      |

## **⚙️ Setup & Installation Instructions**

Follow these steps to get the project running locally:

### **1\. Clone the Repository**

git clone \[https://github.com/\](https://github.com/)\<your-username\>/Hr-leave-management-system.git  
cd Hr-leave-management-system

### **2\. Database Setup**

1. Create a MySQL database (e.g., olms_db).
2. Execute the necessary SQL schema creation script (usually found in db/schema.sql or similar path) to create the users, leaves, and activity_log tables.
3. Update the **JDBC connection details** (URL, Username, Password) in the designated configuration file (e.g., a Config class or directly in the Database.java file).

### **3\. Compile and Run**

1. Ensure you have the **MySQL Connector/J JAR** file added to your project's classpath (lib/mysql-connector-java.jar).
2. Compile the Java source files (src/olms/\*.java).
3. Run the main class (likely Login.java or EmployeeApp.java) from your IDE or the command line.

\# Example run command (assuming you have compiled to a 'bin' directory)  
java \-cp .:lib/mysql-connector-java.jar olms.EmployeeApp
