package core;

import java.time.LocalDate;

/**
 * Represents a payslip for an employee, including details like gross and net pay,
 * and the payroll generation date.
 * This class is used to store and display payroll information for an employee.
 */
public class Payslip {
    private String employeeId; // Employee ID linked to payroll
    private LocalDate date;    // Payroll generation date
    private double grossPay;   // Total salary before deductions
    private double netPay;     // Salary after deductions (income tax, insurance, etc.)

    /**
     * Initializes a new payslip for an employee with the given gross and net pay.
     * The current date is automatically set as the payroll generation date.
     *
     * @param employeeId The unique identifier for the employee.
     * @param grossPay The total salary before deductions.
     * @param netPay The salary after deductions.
     */
    public Payslip(String employeeId, double grossPay, double netPay) {
        this.employeeId = employeeId; // Set the employee ID
        this.date = LocalDate.now();  // Set the current date as the payroll date
        this.grossPay = grossPay;
        this.netPay = netPay;
    }

    /**
     * Returns a string representation of the payslip, including the employee ID,
     * payroll generation date, gross salary, and net salary.
     *
     * @return A string containing the formatted payslip details.
     */
    @Override
    public String toString() {
        return "Payroll{ \n" +
                "Employee ID: " + employeeId + "\n" +
                "Date: " + date + "\n" +
                "Gross salary: " + grossPay + "\n" +
                "Net salary: " + netPay + "\n" +
                '}';
    }
}
