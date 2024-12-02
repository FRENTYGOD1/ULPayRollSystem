package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The TaxCalculation class handles the calculation of taxes based on gross salary,
 * as well as the display of salary details including deductions and net pay.
 */
public class TaxCalculation {
    private double taxPayable; // Basic tax payable

    /**
     * Calculates the tax payable based on the gross salary.
     * It uses a progressive tax rate system: a lower tax rate applies to earnings up to a threshold,
     * and a higher tax rate applies to earnings above that threshold.
     *
     * @param grossPay The gross salary of the employee.
     * @return The total tax payable on the gross salary.
     */
    public double calculateTax(double grossPay) {
        double lowTaxRateThreshold = 42000; // €42,000 lower tax rate limit
        double lowTaxRate = 0.2;
        double highTaxRate = 0.4;

        // Calculate tax payable
        if (grossPay <= lowTaxRateThreshold) {
            taxPayable = grossPay * lowTaxRate; // Low tax rate
        } else {
            taxPayable = lowTaxRateThreshold * lowTaxRate; // Low tax portion
            taxPayable += (grossPay - lowTaxRateThreshold) * highTaxRate; // High tax portion
        }
        return taxPayable;
    }

    /**
     * Displays salary details based on position and scale point.
     * This includes annual salary, monthly salary (before tax), monthly tax deduction,
     * and monthly net pay (after tax).
     *
     * @param position The position of the employee.
     * @param scalePoint The scale point of the employee.
     * @param filePath The file path to the salary data CSV.
     */
    public void displaySalaryDetails(String position, String scalePoint, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Split CSV file data by comma
                if (values.length >= 4 &&
                        values[1].trim().equalsIgnoreCase(position) &&
                        values[2].trim().equals(scalePoint)) {

                    // Parse salary from the CSV file
                    String salaryString = values[3].replaceAll("[^\\d.]", "").trim();
                    double annualSalary = Double.parseDouble(salaryString);
                    double monthlySalary = annualSalary / 12;
                    double monthlyTax = calculateTax(annualSalary) / 12; // Monthly tax
                    double monthlyNetPay = monthlySalary - monthlyTax;

                    // Display the salary details
                    System.out.printf("Annual Salary: €%,.2f%n", annualSalary);
                    System.out.printf("Monthly Salary (Before Tax): €%,.2f%n", monthlySalary);
                    System.out.printf("Monthly Tax Deduction: €%,.2f%n", monthlyTax);
                    System.out.printf("Monthly Net Pay (After Tax): €%,.2f%n", monthlyNetPay);
                    return; // Stop processing after finding the match
                }
            }
            System.out.println("Salary details not found for the given position and scale point.");
        } catch (IOException e) {
            System.err.println("Error reading salary data: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing salary data: " + e.getMessage());
        }
    }
}
