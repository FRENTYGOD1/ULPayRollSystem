package core;

import utils.TaxCalculation;


//Base information for full-time employee
public class FullTimeEmployee extends Employee {
    private static double inComeTax ;//tax of employee
    private static final double HEALTH_INSURANCE = 39; // Health insurance
    private static final double UNION_FEES = 30;//union fee
    private static double PRSI;//Social insurance

    private static double grossPay;

    //Initialise Full-Time staff information
    public FullTimeEmployee(String id, String name, String role, double salaryScale) {
        super(id, name, role);//Initialise basic information
        this.salaryScale = salaryScale;//set salary scale
    }


    public static double getDeductions() {
        double grossPay = getSalaryScale() * getCurrentPoint(); // Calculate gross salary


        //calculation of monthly salary
        TaxCalculation taxCalc = new TaxCalculation(); // Create a TaxCalculation instance
        inComeTax = taxCalc.calculateTax(grossPay); // Calculate and store the income tax


        //Judge whether an employee is required to pay PRSI
        if (grossPay / 12 / 4 < 352){//weekly salary < 352, PRSI free
            PRSI = 0;
        }else{//weekly salary > 352
            PRSI = (grossPay / 12 / 4 *0.041 - 12) * 4;
        }

        return HEALTH_INSURANCE + UNION_FEES + PRSI + inComeTax;
    }


    // Calculation of monthly salary
    @Override
    public double calculateMonthlyPay() {
        grossPay = getSalaryScale() * getCurrentPoint(); // Calculate gross salary
        double deductions = getDeductions(); // Use the getDeductions method
        return grossPay - deductions; // Return net salary
    }

    public static double getGrossPay() {
        return grossPay; // Return gross pay
    }

}

