package core;

/**
 * Abstract base class for employee types, containing common properties and methods.
 * This class defines essential employee information such as name, id, role, salary scale, and promotion level.
 * It also provides methods for calculating the monthly salary, promoting employees, and displaying employee details.
 */
public abstract class Employee {

    // Protected fields for employee details, accessible to subclasses
    protected String name;           // Employee's name
    protected String id;             // Employee's ID
    protected String role;           // Full-time or Part-time
    protected static double salaryScale;    // Salary scale
    protected static int currentPoint;      // Current promotion level or point of the employee

    /**
     * Constructor to initialize basic employee information.
     *
     * @param id The employee's ID
     * @param name The employee's name
     * @param role The role of the employee (e.g., full-time, part-time)
     */
    public Employee(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    /**
     * Abstract method to calculate the employee's monthly pay.
     * This method must be implemented by subclasses to calculate the specific monthly pay.
     *
     * @return The employee's monthly pay
     */
    public abstract double calculateMonthlyPay();

    /**
     * Promotes the employee to the next promotion level.
     * The promotion level is incremented by one.
     */
    public void promoteToNextPoint() {
        currentPoint++; // Increment promotion level
        System.out.println("Promotion to points: " + currentPoint);
    }

    /**
     * Gets the employee's ID.
     *
     * @return The employee's ID
     */
    public String getid() {
        return id;
    }

    /**
     * Gets the employee's name.
     *
     * @return The employee's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the employee's role.
     *
     * @return The employee's role
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the salary scale.
     *
     * @return The current salary scale for employees
     */
    public static double getSalaryScale() {
        return salaryScale;
    }

    /**
     * Gets the current promotion level of the employee.
     *
     * @return The current promotion level of the employee
     */
    public static int getCurrentPoint() {
        return currentPoint;
    }

    /**
     * Displays the details of the employee, including ID, name, and role.
     */
    public void displayDetails() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
    }
}
