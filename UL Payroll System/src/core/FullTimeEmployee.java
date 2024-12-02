package core;

import utils.TaxCalculation;

/**
 * Represents a full-time employee with additional salary details, including income tax, health insurance,
 * union fees, and social insurance (PRSI).
 * This class extends the abstract `Employee` class and provides specific implementation for full-time employees,
 * including salary calculation and deductions.
 */
public class FullTimeEmployee extends Employee {
    private static double inComeTax; // Tax of employee
    private static final double HEALTH_INSURANCE = 39; // Health insurance
    private static final double UNION_FEES = 30; // Union fee
    private static double PRSI; // Social insurance

    private static double grossPay; // Gross salary

    /**
     * Initializes a full-time employee with basic information and a salary scale.
     *
     * @param id The employee's ID
     * @param name The employee's name
     * @param role The employee's role (should be "Full-time")
     * @param salaryScale The employee's salary scale
     */
    public FullTimeEmployee(String id, String name, String role, double salaryScale) {
        super(id, name, role); // Initialize basic information from the superclass
        this.salaryScale = salaryScale; // Set the salary scale
    }

    /**
     * Calculates the deductions for a full-time employee, including income tax, health insurance, union fees,
     * and social insurance (PRSI).
     *
     * @return The total deductions for the employee
     */
    public static double getDeductions() {
        double grossPay = getSalaryScale() * getCurrentPoint(); // Calculate gross salary

        // Create an instance of TaxCalculation to calculate income tax
        TaxCalculation taxCalc = new TaxCalculation();
        inComeTax = taxCalc.calculateTax(grossPay); // Calculate and store income tax

        // Determine if the employee is required to pay PRSI
        if (grossPay / 12 / 4 < 352) { // Weekly salary < 352, PRSI free
            PRSI = 0;
        } else { // Weekly salary >= 352
            PRSI = (grossPay / 12 / 4 * 0.041 - 12) * 4;
        }

        // Return the total deductions (health insurance + union fees + PRSI + income tax)
        return HEALTH_INSURANCE + UNION_FEES + PRSI + inComeTax;
    }

    /**
     * Calculates the employee's monthly salary after deductions.
     * The deductions are calculated using the `getDeductions` method.
     *
     * @return The employee's monthly net salary (gross pay - deductions)
     */
    @Override
    public double calculateMonthlyPay() {
        grossPay = getSalaryScale() * getCurrentPoint(); // Calculate gross salary
        double deductions = getDeductions(); // Get deductions
        return grossPay - deductions; // Return net salary
    }

    /**
     * Gets the gross salary (before deductions) of the employee.
     *
     * @return The gross salary of the employee
     */
    public static double getGrossPay() {
        return grossPay; // Return gross pay
    }
}
