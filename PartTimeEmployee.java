package core;

import core.Employee;

public class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private int hoursWork;

    //constructor
    public PartTimeEmployee(String id, String name, String role, double hourlyRate) {
        super(id, name, role);
        this.hourlyRate = hourlyRate;
    }

    //set working times
    public void setHoursWork(int hoursWork){
        this.hoursWork = hoursWork;
    }

    //calculate salary
    @Override
    public double calculateMonthlyPay(){
        return hourlyRate * hoursWork;
    }

    @Override
    public void displayDetails(){
        super.displayDetails();
        System.out.println("Hourly Rate: " + hourlyRate);
        System.out.println("working hours: " + hoursWork);
    }
}
//思考兼职如何计算工资
