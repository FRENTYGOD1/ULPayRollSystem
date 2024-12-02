package core;

/**
 * Represents a part-time employee with salary details based on hourly rate and hours worked.
 * This class extends the abstract `Employee` class and provides specific implementation for part-time employees,
 * including salary calculation, working hour details, and salary claim requirement.
 */
public class PartTimeEmployee extends Employee {
    private double hourlyRate; // Hourly rate (salary)
    private int hoursWorked; // Number of hours worked for the month

    /**
     * Initializes a part-time employee with basic information and hourly rate.
     *
     * @param id The employee's ID
     * @param name The employee's name
     * @param role The employee's role (should be "Part-time")
     * @param hourlyRate The hourly rate of the employee
     */
    public PartTimeEmployee(String id, String name, String role, double hourlyRate) {
        super(id, name, role); // Call the parent class constructor to initialize basic information
        this.hourlyRate = hourlyRate; // Set the hourly rate
    }

    /**
     * Calculates the monthly salary for the part-time employee based on hourly rate and hours worked.
     *
     * @return The monthly salary (hourly rate * hours worked)
     */
    @Override
    public double calculateMonthlyPay() {
        return hourlyRate * hoursWorked; // Calculate monthly pay
    }

    /**
     * Determines if a salary claim form is required for the part-time employee.
     * A salary claim form is required if the employee works less than 40 hours in a month.
     *
     * @return true if the salary claim form is required (hours worked < 40), false otherwise
     */
    public boolean isSalaryClaimRequired() {
        return hoursWorked < 40; // If hours worked < 40, salary claim form is required
    }

    /**
     * Displays the details of the part-time employee, including hourly rate, working hours,
     * salary, and whether a salary claim form is required.
     */
    @Override
    public void displayDetails() {
        super.displayDetails(); // Call the parent class method to display basic details
        System.out.println("Hourly Rate: " + hourlyRate); // Display hourly rate
        System.out.println("Working Hours: " + hoursWorked); // Display working hours
        System.out.println("Salary: " + calculateMonthlyPay()); // Display salary
        System.out.println("Is Salary Claim Form Required? " + (isSalaryClaimRequired() ? "Yes" : "No")); // Display if form is required
    }

    // Getter and setter methods

    /**
     * Gets the hourly rate of the part-time employee.
     *
     * @return The hourly rate
     */
    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Sets the hourly rate of the part-time employee.
     *
     * @param hourlyRate The hourly rate to set
     */
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    /**
     * Gets the number of hours worked by the part-time employee for the month.
     *
     * @return The number of hours worked
     */
    public int getHoursWorked() {
        return hoursWorked;
    }

    /**
     * Sets the number of hours worked by the part-time employee for the month.
     *
     * @param hoursWorked The number of hours worked to set
     */
    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}
