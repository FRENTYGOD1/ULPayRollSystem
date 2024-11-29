package core;

import core.FullTimeEmployee;
import core.Employee;

import java.util.ArrayList;
import java.util.List;

//Payroll system class, responsible for managing employees and generating payslips
public class PayrollSystem {
    private List<Employee> employees;
    private List<Payslip> payslips;

    //Initialisation list
    public PayrollSystem(){
        this.employees = new ArrayList<>();
        this.payslips = new ArrayList<>();
    }

    //add new staffs
    public void addStaff(Employee employee){
        employees.add(employee);//add staff into list
        System.out.println("core.Staff Added: " + employee.getName());//output confirm information
    }

    //Generate payroll for all employees
    public void generatePayslips(){
        for (Employee employee : employees){//Iterate through the staff list
            double netPay = employee.calculateMonthlyPay();//calc net salary
            Payslip payslip = new Payslip(employee.getid(), employee.getSalaryScale(), netPay);//create payslip
            payslips.add(payslip);//add payslip to the list
            System.out.println(payslip);
        }
    }

    //promote full-time staff
    public void promoteFullTimeStaff(String id){//Iterate through the staff list
        for (Employee employee: employees){
            if (employee instanceof FullTimeEmployee && employee.getid().equals(id)){
                //Check if you are a full-time employee and match the number
                employee.promoteToNextPoint();//promote to the next point
                break;
            }
        }
    }
}
