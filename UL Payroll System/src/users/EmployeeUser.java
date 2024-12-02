package users;

/**
 * The EmployeeUser class represents an employee user in the system. It holds the employee's username
 * and provides functionality for displaying the employee's menu options.
 */
public class EmployeeUser {
    // Employee's username
    private String username;

    /**
     * Constructor to create an EmployeeUser object with a specific username.
     *
     * @param username The username of the employee.
     */
    public EmployeeUser(String username) {
        this.username = username; // Set employee's username
    }

    /**
     * Displays the employee's menu options.
     * This method currently provides options for viewing the payslip.
     * Additional options can be added as needed.
     */
    public void menu() {
        System.out.println("Employee Menu:");
        System.out.println("Viewing Payslip..."); // Export View Payroll Options
    }
}
