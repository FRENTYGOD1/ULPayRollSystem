package core;

public abstract class Employee {
    protected String name;
    protected String id;
    protected String role;//full-time or part-time
    protected double salaryScale;
    protected int currentPoint;

    //Initialise basic staff information
    public Employee(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    //Abstract method payroll calculation
    public abstract double calculateMonthlyPay();


    //Employees are promoted to the next point
    public void promoteToNextPoint(){
        currentPoint++;//increase by degrees
        System.out.println("Promotion to points: " + currentPoint);
    }

    // Logic to fetch and display payslip
    public void viewPayslip() {
        System.out.println("Displaying payslip for employee: " + name);
    }

    //get method
    public String getid(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getRole(){
        return role;
    }
    public double getSalaryScale(){
        return salaryScale;
    }
    public int getCurrentPoint(){
        return currentPoint;
    }


    //Print Employee Details
    public void displayDetails(){
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
    }
}
