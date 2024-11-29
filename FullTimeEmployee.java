package core;

import utils.TaxCalculation;

public class FullTimeEmployee extends Employee {
    private double inComeTax ;
    private static final double HEALTH_INSURANCE = 39; // Health insurance costs
    private static final double UNION_FEES = 30;//union fee
    //private static final double USC =
    private double PRSI;

    //constructor,Initialise Full-Time staff information
    public FullTimeEmployee(String id, String name, String role, double salaryScale) {
        super(id, name, role);
        this.salaryScale = salaryScale;
    }

    //calculation of monthly salary
    //insurance
    @Override
    public double calculateMonthlyPay(){
        double grossPay = getSalaryScale() * getCurrentPoint();//calc gross salary
        TaxCalculation taxCalc = new TaxCalculation(); // Create an instance of TaxCalculation
        inComeTax = taxCalc.calculateTax(grossPay); // Calculate and store the income tax

        if (grossPay / 12 / 4 < 352){//weekly salary < 352, PRSI free
            PRSI = 0;
        }else{//weekly salary > 352
            PRSI = (grossPay / 12 / 4 *0.041 - 12) * 4;
        }

        double deductions = HEALTH_INSURANCE + UNION_FEES + PRSI + inComeTax;//calc chargebacks
        return grossPay - deductions;//Return to net salary
    }










    //promote point
    public void promote() {
        System.out.println(name + " promoted!");
        salaryScale += 1; // Increment salary scale
    }
}

