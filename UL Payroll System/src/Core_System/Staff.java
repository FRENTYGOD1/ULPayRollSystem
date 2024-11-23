public abstract class Staff {
    private String name;
    private String id;
    private String role;//full-time or part-time
    private double salaryScale;
    private int currentPoint;

    //Initialise basic staff information
    public Staff(String id, String name, String role, double salaryScale, int currentPoint) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.salaryScale = salaryScale;
        this.currentPoint = currentPoint;
    }

    //Abstract method payroll calculation
    public abstract double calculateMonthlyPay();

    //Employees are promoted to the next point
    public void promoteToNextPoint(){
        currentPoint++;//increase by degrees
        System.out.println("Promotion to points: " + currentPoint);
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
//完成
