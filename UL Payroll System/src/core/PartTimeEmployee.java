package core;

public class PartTimeEmployee extends Employee {
    private double hourlyRate; // Hourly rate (salary)
    private int hoursWorked; // Number of hours worked for the month

    // Constructor to initialize part-time employee information
    public PartTimeEmployee(String id, String name, String role, double hourlyRate) {
        super(id, name, role); // Call the parent class constructor to initialize basic information
        this.hourlyRate = hourlyRate;
    }

    // Method to calculate monthly pay
    @Override
    public double calculateMonthlyPay() {
        return hourlyRate * hoursWorked;
    }

    // Method to check if a salary claim form is required
    public boolean isSalaryClaimRequired() {
        return hoursWorked < 40; // If hours worked < 40, salary claim form is required
    }

    // Method to display part-time employee details
    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Hourly Rate: " + hourlyRate);
        System.out.println("Working Hours: " + hoursWorked);
        System.out.println("Salary: " + calculateMonthlyPay());
        System.out.println("Is Salary Claim Form Required? " + (isSalaryClaimRequired() ? "Yes" : "No"));
    }

    // Getter and setter methods
    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}
