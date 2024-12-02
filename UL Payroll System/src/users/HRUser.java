package users;

import utils.PromotionManager;
import java.util.Scanner;

public class HRUser {

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Employee ID to promote: ");
        String employeeId = scanner.nextLine().trim();


        PromotionManager.promoteEmployeeById(employeeId);
    }
}
