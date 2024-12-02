package users;

import utils.PromotionManager;
import java.util.Scanner;

/**
 * The HRUser class represents an HR user in the system.
 * It allows the HR user to promote an employee by entering their ID.
 */
public class HRUser {

    /**
     * Displays the HR menu and allows the user to promote an employee by entering their ID.
     * This method prompts the HR user to input an employee's ID and uses the PromotionManager
     * to handle the promotion process.
     */
    public void menu() {
        // Create a scanner to read user input
        Scanner scanner = new Scanner(System.in);

        // Prompt user to enter the employee's ID
        System.out.print("Enter Employee ID to promote: ");
        String employeeId = scanner.nextLine().trim(); // Read and trim the input

        // Promote the employee by their ID using PromotionManager
        PromotionManager.promoteEmployeeById(employeeId);
    }
}
