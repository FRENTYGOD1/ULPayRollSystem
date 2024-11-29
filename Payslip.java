package core;

import java.time.LocalDate;

//Payroll category, which holds employee payroll information
public class Payslip {
    private String employeeId;//core.Staff number associated with payroll
    private LocalDate date;//Payroll generation date
    private double grossPay;//total salary for a year
    private double netPay;


    //Initialising payroll
    public Payslip(String employeeId, double grossPay, double netPay){
        this.employeeId = employeeId;
        this.date = LocalDate.now();//Set the current date as the payroll date
        this.grossPay = grossPay;
        this.netPay = netPay;
    }

    //Exporting payroll information
    @Override
    public String toString(){
        return "Payroll{ " + "core.Staff ID:" + employeeId + '\'' + ", Date: " + date + ", Gross salary: " + grossPay + ", Net salary: " + netPay + '}';
    }
}
//完成
