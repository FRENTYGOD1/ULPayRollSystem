package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TaxCalculation {
    private double taxPayable;//basic tax-payable

    //Calculation of taxes
    public double calculateTax(double grossPay) {
        double lowTaxRateThreshold = 42000; // €42,000 lower tax rate limit
        double lowTaxRate = 0.2;
        double highTaxRate = 0.4;

        //the part of low tax
        if (grossPay <= lowTaxRateThreshold) {
            taxPayable = grossPay * lowTaxRate;
        } else {//high tax
            //the part of low
            taxPayable = lowTaxRateThreshold * lowTaxRate;
            //the part of high
            taxPayable += (grossPay - lowTaxRateThreshold) * highTaxRate;
        }
        return taxPayable;
    }

    // Method to display salary details based on position and scale point
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
