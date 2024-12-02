package core;


//Base class for employee types, containing common properties and methods
public abstract class Employee {
    // Protected fields for employee details, accessible to subclasses
    protected String name;           //Employee's name
    protected String id;             //Employee's ID
    protected String role;           //Full-time or Part-time
    protected static double salaryScale;    //Salary scale
    protected static int currentPoint;      //Current promotion level or point of the employee


    //Initialise basic employee information
    public Employee(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }


    //Calculate employee's salary
    public abstract double calculateMonthlyPay();


    //Employees are promoted to the next grade
    public void promoteToNextPoint() {
        currentPoint++; // Increment promotion level
        System.out.println("Promotion to points: " + currentPoint);
    }


    //Get Employee Details
    public String getid() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public static double getSalaryScale() {
        return salaryScale;
    }

    public static int getCurrentPoint() {
        return currentPoint;
    }


    //Print Employee Details
    public void displayDetails() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
    }
}

