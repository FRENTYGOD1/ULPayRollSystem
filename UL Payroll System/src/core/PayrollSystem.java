package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a payroll system for managing employees and generating payslips.
 * This class is responsible for loading employee data from CSV files, generating payroll for employees,
 * and retrieving payslips by employee ID.
 */
public class PayrollSystem {
    private List<Employee> employees; // List of employees
    private List<Payslip> payslips;   // List of payslips
    private static final String EMPLOYEE_DATA_CSV = "src/resource/csv/EmployeeData.csv";  // Path to employee data CSV
    private static final String POSITION_DATA_CSV = "src/resource/csv/Position_Scale.csv";  // Path to position scale data CSV

    /**
     * Initializes the PayrollSystem object with empty employee and payslip lists.
     */
    public PayrollSystem(){
        this.employees = new ArrayList<>(); // Initialize employee list
        this.payslips = new ArrayList<>();   // Initialize payslip list
    }

    /**
     * Loads employee data from a CSV file and adds the employees to the employee list.
     * The method parses the CSV file, extracts employee details, and creates an `Employee` object for each record.
     */
    public void loadEmployeeData() {
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DATA_CSV))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String id = values[1].trim();       // Employee ID
                String name = values[0].trim();     // Employee name
                String role = values[2].trim();     // Employee role
                double salaryScale = Double.parseDouble(values[4].trim()); // Salary scale

                // Create Employee object and add it to the employee list
                Employee employee = new FullTimeEmployee(id, name, role, salaryScale);
                employees.add(employee);
            }
        } catch (IOException e) {
            System.out.println("Error loading employee data: " + e.getMessage());
        }
    }

    /**
     * Generates payslips for all employees by calculating their net pay and creating payslip objects.
     * Each payslip is added to the payslips list and printed to the console.
     */
    public void generatePayslips(){
        for (Employee employee : employees) { // Iterate through employee list
            double netPay = employee.calculateMonthlyPay(); // Calculate net salary
            Payslip payslip = new Payslip(employee.getid(), employee.getSalaryScale(), netPay); // Create payslip
            payslips.add(payslip); // Add payslip to the list
            System.out.println(payslip); // Print payslip
        }
    }

    /**
     * Retrieves the payslip for an employee based on the employee ID.
     * It reads the employee and position data CSVs, matches the employee ID, and calculates the gross and net pay.
     *
     * @param employeeId The ID of the employee whose payslip is to be retrieved.
     * @return A `Payslip` object for the employee, or `null` if no matching employee is found.
     */
    public Payslip getPaySlipById(String employeeId) {
        double grossPay = FullTimeEmployee.getGrossPay();
        double deduction = FullTimeEmployee.getDeductions(); // Fixed deduction amount
        String position = "";  // To store position name
        String scalePoint = ""; // To store scale_point

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DATA_CSV))) {
            String line;
            br.readLine(); // Skip header line

            // Traverse CSV file to find employee matching the input ID
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Check if employee ID matches
                if (values[1].trim().equals(employeeId)) {
                    position = values[2].trim(); // Position name
                    scalePoint = values[4].trim(); // Scale point

                    // If matching employee is found, get corresponding salary
                    if (scalePoint != null && !scalePoint.isEmpty()) {
                        grossPay = getSalaryByScalePoint(scalePoint); // Get salary by scale_point
                    } else {
                        grossPay = getSalaryByPosition(position); // Get salary by position
                    }

                    double netPay = grossPay - deduction; // Calculate net salary
                    return new Payslip(employeeId, grossPay, netPay); // Return payslip
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading EmployeeData.csv: " + e.getMessage());
        }

        return null; // Return null if no matching employee found
    }

    /**
     * Retrieves the salary based on the scale point from the `Position_Scale.csv` file.
     *
     * @param scalePoint The scale point used to retrieve the salary.
     * @return The salary corresponding to the provided scale point, or 0.0 if no match is found.
     */
    private double getSalaryByScalePoint(String scalePoint) {
        try (BufferedReader br = new BufferedReader(new FileReader(POSITION_DATA_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Check if the row has enough columns
                if (values.length > 3 && values[2].trim().equals(scalePoint)) { // Match scale point
                    return Double.parseDouble(values[3].trim().replace("\"", "")); // Remove quotes and parse salary
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Position_Scale.csv: " + e.getMessage());
        }
        return 0.0; // Return 0.0 if no matching scale point is found
    }

    /**
     * Retrieves the salary based on the position from the `Position_Scale.csv` file.
     *
     * @param position The position used to retrieve the salary.
     * @return The salary corresponding to the provided position, or 0.0 if no match is found.
     */
    private double getSalaryByPosition(String position) {
        try (BufferedReader br = new BufferedReader(new FileReader(POSITION_DATA_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Check if the row has enough columns
                if (values.length > 3 && values[1].trim().equals(position)) { // Match position
                    return Double.parseDouble(values[3].trim().replace("\"", "")); // Remove quotes and parse salary
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Position_Scale.csv: " + e.getMessage());
        }
        return 0.0; // Return 0.0 if no matching position is found
    }
}
