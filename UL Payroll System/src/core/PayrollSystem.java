package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//Payroll system class, responsible for managing employees and generating payslips
public class PayrollSystem {
    private List<Employee> employees;//employee list
    private List<Payslip> payslips;//payroll list
    private static final String EMPLOYEE_DATA_CSV = "src/resource/csv/EmployeeData.csv";
    private static final String POSITION_DATA_CSV = "src/resource/csv/Position_Scale.csv";
    public void loadEmployeeData() {
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DATA_CSV))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String id = values[1].trim();
                String name = values[0].trim();
                String role = values[2].trim();
                double salaryScale = Double.parseDouble(values[4].trim());

                // Create Employee object and add it to the employee list
                Employee employee = new FullTimeEmployee(id, name, role, salaryScale);
                employees.add(employee);
            }
        } catch (IOException e) {
            System.out.println("Error loading employee data: " + e.getMessage());
        }
    }
    //Initialise the list of employees and payroll
    //Create empty employee list and payroll list
    public PayrollSystem(){
        this.employees = new ArrayList<>();//Initialise employee list
        this.payslips = new ArrayList<>();//Initialise the payroll list
    }


    //Generate payroll for all employees
    public void generatePayslips(){
        for (Employee employee : employees){//Iterate through the employee list
            double netPay = employee.calculateMonthlyPay();//calculate net salary
            Payslip payslip = new Payslip(employee.getid(), employee.getSalaryScale(), netPay);//create payslip
            payslips.add(payslip);//add payslip to the list
            System.out.println(payslip);
        }
    }

    public Payslip getPaySlipById(String employeeId) {
        double grossPay = FullTimeEmployee.getGrossPay();
        double deduction = FullTimeEmployee.getDeductions(); // Fixed deduction amount, can be adjusted if needed
        String position = ""; // To store the position name
        String scalePoint = ""; // To store the scale_point

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DATA_CSV))) {
            String line;
            br.readLine(); // Skip header line

            // Traverse the CSV file and find the employee matching the input
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Check if employee ID matches
                if (values[1].trim().equals(employeeId)) {  // Assuming the 2nd column is employeeId
                    position = values[2].trim();  // Assuming the 3rd column is the position name
                    scalePoint = values[4].trim();  // Assuming the 5th column is scale_point

                    // If a matching employee is found, get the corresponding salary
                    if (scalePoint != null && !scalePoint.isEmpty()) {
                        grossPay = getSalaryByScalePoint(scalePoint);  // Get salary based on scale_point
                    } else {
                        grossPay = getSalaryByPosition(position);  // Get salary based on position
                    }

                    double netPay = grossPay - deduction; // Calculate net salary
                    return new Payslip(employeeId, grossPay, netPay); // Return payslip
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading EmployeeData.csv: " + e.getMessage());
        }

        return null; // If no matching employee is found, return null
    }

    private double getSalaryByScalePoint(String scalePoint) {
        try (BufferedReader br = new BufferedReader(new FileReader(POSITION_DATA_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Check if the row has enough columns
                if (values.length > 3 && values[2].trim().equals(scalePoint)) {  // Get salary based on scale_point
                    return Double.parseDouble(values[3].trim().replace("\"", "")); // Remove quotes and parse the salary
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Position_Scale.csv: " + e.getMessage());
        }
        return 0.0;  // If no matching scale_point is found, return 0.0
    }

    // Method to get salary based on position
    private double getSalaryByPosition(String position) {
        try (BufferedReader br = new BufferedReader(new FileReader(POSITION_DATA_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Check if the row has enough columns
                if (values.length > 3 && values[1].trim().equals(position)) {  // Get salary based on position
                    return Double.parseDouble(values[3].trim().replace("\"", "")); // Remove quotes and parse the salary
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Position_Scale.csv: " + e.getMessage());
        }
        return 0.0;  // If no matching position is found, return 0.0
    }




}
