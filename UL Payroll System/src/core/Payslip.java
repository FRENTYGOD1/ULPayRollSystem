package core;

import java.time.LocalDate;


public class Payslip {
    private String employeeId;//Employee number linked to payroll
    private LocalDate date;//Payroll generation date
    private double grossPay;//total salary for a year
    private double netPay;//Income after tax


    //Initialise payroll information
    public Payslip(String employeeId, double grossPay, double netPay) {
        this.employeeId = employeeId;//set employee ID
        this.date = LocalDate.now();//Set the current date as the payroll date
        this.grossPay = grossPay;
        this.netPay = netPay;
    }



    //Exporting payroll information
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

