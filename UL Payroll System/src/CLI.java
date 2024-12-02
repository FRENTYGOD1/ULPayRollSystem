import java.util.Scanner;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Paths;

import core.PartTimeEmployee;
import core.PayrollSystem;
import core.Payslip;
import database.DatabaseMenu;
import database.DatabaseUtility;
import users.HRUser;
import utils.TaxCalculation;

public class CLI {

    // PayrollSystem is used for staff management
    private static final PayrollSystem payrollSystem = new PayrollSystem();
    // DatabaseMenu manages database-related functions
    private static final DatabaseMenu databaseMenu = new DatabaseMenu();
    // DatabaseUtility helps to access the CSV files
    private static final DatabaseUtility dbUtility = new DatabaseUtility();
    // userCredentials saves username, password, and role information
    private static final Map<String, String[]> userCredentials = new HashMap<>();

    public static void main(String[] args) {
        // Load usernames and passwords
        loadCredentials();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Main loop for login and user interaction
        while (running) {
            System.out.println("Welcome to the UL Payroll System. Please follow the prompts to proceed.");
            System.out.println("Please enter your username and password. (Enter '0' to exit)");

            boolean authenticated = false;
            String role = "";
            String name = "";

            // Login loop, keeps asking until user logs in or exits
            while (!authenticated) {
                System.out.print("Name: ");
                name = scanner.nextLine().trim();
                if (name.equals("0")) {
                    System.out.println("Exiting the system...");
                    running = false;
                    break;
                }

                System.out.print("Password: ");
                String password = scanner.nextLine().trim();
                role = authenticate(name, password);
                if (role != null) {
                    authenticated = true;
                    System.out.println("Login successful, welcome " + name + "!");
                    // Go to main menu after successful login
                    handleMainMenu(scanner, role, name);
                } else {
                    System.out.println("Login failed. Please try again.");
                }
            }
        }
        scanner.close();
    }

    // Main menu for different roles like HR, employee, etc.
    private static void handleMainMenu(Scanner scanner, String role, String username) {
        boolean stayInMenu = true;
        while (stayInMenu) {
            displayCurrentDateTime(); // Show current time
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. View Personal Information");
            System.out.println("2. View Salary Details");
            if (role.equals("HR")) { // Special options for HR role
                System.out.println("3. HR Functions");
                System.out.println("4. Manage Database");
            }
            System.out.println("0. Log Out");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            // Options for the main menu
            switch (input) {
                case "1":
                    showPersonalInformationMenu(scanner, username); // Show personal info
                    break;
                case "2":
                    showSalaryDetailsMenu(scanner, username); // Show salary info
                    break;
                case "3":
                    if (role.equals("HR")) manageHrTasks(scanner); // HR tasks
                    break;
                case "4":
                    if (role.equals("HR")) manageDatabaseMenu(scanner); // Database options
                    break;
                case "0":
                    stayInMenu = false; // Log out
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Load user data from CSV files (username, password, role)
    private static void loadCredentials() {
        String[] files = {"EmployeeData.csv", "HRData.csv"};
        for (String file : files) {
            String filePath = Paths.get(dbUtility.getDefaultCsvPath(), file).toString();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length >= 8) {
                        String username = values[0].trim().toLowerCase();
                        String password = values[6].trim();
                        String role = getRoleFromAccessLevel(values[7].trim());
                        userCredentials.put(username, new String[]{password, role});
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading credentials from " + file + ": " + e.getMessage());
            }
        }
    }

    // Get user role based on access level from CSV
    private static String getRoleFromAccessLevel(String accessLevel) {
        switch (accessLevel) {
            case "1":
                return "Admin";
            case "2":
                return "Manager";
            case "3":
                return "Employee";
            case "4":
                return "HR";
            case "5":
                return "Co-op Student";
            default:
                return null;
        }
    }

    // Check if username and password are correct
    private static String authenticate(String username, String password) {
        String normalizedUsername = username.toLowerCase();
        if (userCredentials.containsKey(normalizedUsername) && userCredentials.get(normalizedUsername)[0].equals(password)) {
            return userCredentials.get(normalizedUsername)[1];
        }
        return null;
    }

    // Display the current date and time
    private static void displayCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("\nCurrent Date and Time: " + currentDateTime.format(formatter));
    }

    // Show user personal information
    private static void showPersonalInformationMenu(Scanner scanner, String username) {
        System.out.println("\n===== Personal Information Menu =====");
        displayEmployeeDetails(username);
        System.out.println("0. Back to Main Menu");
        scanner.nextLine(); // Wait for user input
    }

    // Show user salary details
    private static void showSalaryDetailsMenu(Scanner scanner, String username) {
        System.out.println("\n===== Salary Details Menu =====");
        if (!displaySalaryDetails(username)) {
            System.out.println("Salary details not found.");
        }
        System.out.println("0. Back to Main Menu");
        scanner.nextLine(); // Wait for user input
    }

    // Handle HR-specific tasks like promotions or payslips
    private static void manageHrTasks(Scanner scanner) {
        boolean stayInMenu = true;
        while (stayInMenu) {
            System.out.println("\n===== HR Management Menu =====");
            System.out.println("1. Promote Staff");
            System.out.println("2. Generate Payslips");
            System.out.println("3. Manage Part-Time Salary");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");

            String action = scanner.nextLine();

            switch (action) {
                case "1":
                    HRUser hrUser = new HRUser();
                    hrUser.menu();  // Promote staff
                    break;

                case "2":
                    System.out.print("Enter employee ID to generate payslip: ");
                    String employeeId = scanner.nextLine();  // Take input for employee ID

                    // 调用根据 employeeId 获取工资单的方法
                    Payslip payslip = payrollSystem.getPaySlipById(employeeId);

                    if (payslip != null) {
                        System.out.println("Payslip Details:");
                        System.out.println(payslip.toString());
                    } else {
                        System.out.println("Error: No employee found with the given ID.");
                    }

                    // 生成所有员工的工资单
                    payrollSystem.generatePayslips();
                    break;

                case "3":
                    handlePartTimeSalary(scanner);  // Handle part-time salary management
                    break;

                case "0":
                    stayInMenu = false;  // Exit the HR menu
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }




    // Open database menu for HR
    private static void manageDatabaseMenu(Scanner scanner) {
        try {
            databaseMenu.displayDatabaseMenu(scanner);
        } catch (Exception e) {
            System.out.println("Error accessing database menu: " + e.getMessage());
        }
    }

    // Show employee information from CSV
    private static void displayEmployeeDetails(String username) {
        String[] files = {"employeeData.csv", "HRData.csv"};
        for (String file : files) {
            String filePath = Paths.get(dbUtility.getDefaultCsvPath(), file).toString();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values[0].trim().equalsIgnoreCase(username)) {
                        System.out.println("\n===== Employee Details =====");
                        System.out.println("Name: " + values[0]);
                        System.out.println("ID: " + values[1]);
                        System.out.println("Position: " + values[2]);
                        System.out.println("Department: " + values[3]);
                        System.out.println("Scale Point: " + values[4]);
                        System.out.println("Account: " + values[5]);
                        System.out.println("Access Level: " + values[7]);
                        System.out.println("Join Date: " + values[8]);
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading employee data from " + file + ": " + e.getMessage());
            }
        }
        System.out.println("Employee details not found.");
    }

    // Display salary details of the employee
    private static boolean displaySalaryDetails(String username) {
        String[] files = {"EmployeeData.csv", "HRData.csv"};
        String position = "";
        String scalePoint = "";
        for (String file : files) {
            String filePath = Paths.get(dbUtility.getDefaultCsvPath(), file).toString();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values[0].trim().equalsIgnoreCase(username)) {
                        position = values[2];
                        scalePoint = values[4];
                        break;
                    }
                }
                if (!position.isEmpty() && !scalePoint.isEmpty()) {
                    TaxCalculation taxCalc = new TaxCalculation();
                    taxCalc.displaySalaryDetails(position, scalePoint, dbUtility.getDefaultCsvPath() + "/Position_Scale.csv");
                    return true;
                }
            } catch (IOException e) {
                System.out.println("Error reading employee data from " + file + ": " + e.getMessage());
            }
        }
        return false;
    }

    private static void handlePartTimeSalary(Scanner scanner) {
        System.out.print("Enter part-time employee ID: ");
        String employeeId = scanner.nextLine().trim();

        System.out.print("Enter hours worked for the month: ");
        int hoursWorked = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character left by nextInt()

        // Assuming hourlyRate is fixed for part-time employees, we can use it directly
        double hourlyRate = 20.0;  // Example fixed hourly rate, adjust as needed

        // Create a PartTimeEmployee object
        PartTimeEmployee partTimeEmployee = new PartTimeEmployee(employeeId, "PartTime Employee", "PartTime", hourlyRate);
        partTimeEmployee.setHoursWorked(hoursWorked); // Set hours worked for the employee

        // Display employee details and salary info
        partTimeEmployee.displayDetails();

        // Check if a salary claim form is required
        if (partTimeEmployee.isSalaryClaimRequired()) {
            System.out.println("A salary claim form must be submitted.");
        } else {
            System.out.println("No salary claim form required.");
        }
    }


}
