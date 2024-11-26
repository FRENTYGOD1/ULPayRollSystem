package Database_Module;
import java.io.*;
import java.util.*;
import java.nio.file.Paths;
public class DatabaseMenu {

    private static final DatabaseUtility dbu = new DatabaseUtility();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== Database Management Menu =====");
            System.out.println("1. Check the csv list and structures");
            System.out.println("2. Add or Delete Employee");
            System.out.println("3. Backup Data");
            System.out.println("4. Restore Data");
            System.out.println("5. View CSV File Data");
            System.out.println("6. Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    checkCsvListAndStructure(scanner);
                    break;
                case 2:
                    addOrDeleteData(scanner);
                    break;
                case 3:
                    backupData();
                    break;
                case 4:
                    restoreData();
                    break;
                case 5:
                    viewCsvFile(scanner);
                    break;
                case 6:
                    System.out.println("Exiting the system...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, please try again!");
            }
        }
        scanner.close();
    }

    // Method to add or delete data (based on user's choice)
    private static void addOrDeleteData(Scanner scanner) throws IOException {
        System.out.println("Choose an option:");
        System.out.println("1. Add Employee");
        System.out.println("2. Delete Employee");
        System.out.print("Please enter the number of you choose: ");
        int actionChoice = scanner.nextInt();
        scanner.nextLine();  // Clear the buffer

        switch (actionChoice) {
            case 1:
                addData(scanner);  // Add data to CSV file
                break;
            case 2:
                deleteData(scanner);  // Delete data from CSV file
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    //=================================================================================
    //===================================Add Part======================================
    //=================================================================================
    // Method to add new data
    private static void addData(Scanner scanner) throws IOException {
        try {
            String defaultCsvPath = dbu.getDefaultCsvPath();
            String filePath = defaultCsvPath + "/EmployeeData.csv";

            // Read all data in a CSV file
            List<String[]> data = dbu.readCsv(filePath);
            int highestId = 0;

            // Skip the first row (table header) and start processing data from the second row
            for (int i = 1; i < data.size(); i++) { //skip the head
                String[] row = data.get(i);
                try {
                    int currentId = Integer.parseInt(row[1]);
                    highestId = Math.max(highestId, currentId);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing ID in row " + (i + 1) + ": " + row[1]);
                }
            }

            // Auto-generate new values for id, account, password, and join_date
            int newId = highestId + 1;
            String newAccount = "account" + newId;
            String newPassword = "password" + newId;
            String joinDate = java.time.LocalDate.now().toString();  // Current date as "yyyy-MM-dd"

            // Ask for other data (name, position, department, scale_point, access, age)
            System.out.print("\nEnter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter position: ");
            String position = scanner.nextLine();
            System.out.print("Enter department: ");
            String department = scanner.nextLine();
            System.out.print("Enter scale point: ");
            String scalePoint = scanner.nextLine();
            System.out.print("Enter access level: ");
            String access = scanner.nextLine();
            System.out.print("Enter age: ");
            String age = scanner.nextLine();

            // Format the new data as a String array, not a single string
            String[] newData = {
                    name,
                    Integer.toString(newId),
                    position,
                    department,
                    scalePoint,
                    newAccount,
                    newPassword,
                    access,
                    joinDate,
                    age
            };

            // Add the new data to the CSV
            dbu.addDataToCsvFile(filePath, newData);
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
            return;
        }
    }



    // Method to delete data
    //=================================================================================
    //===================================Delete Part===================================
    //=================================================================================
    private static List<String[]> findRowsByName(String filePath, String nameToFind) throws IOException {
        List<String[]> allRows = dbu.readCsv(filePath);
        List<String[]> matchingRows = new ArrayList<>();

        // name is in the first row.
        for (String[] row : allRows) {
            if (row[0].equalsIgnoreCase(nameToFind)) {
                matchingRows.add(row);
            }
        }

        return matchingRows;
    }

    // Method to handle the deletion of data from a selected CSV file
    public static void deleteData(Scanner scanner) throws IOException {
        // Get the default CSV file path using the utility
        String defaultCsvPath = dbu.getDefaultCsvPath();
        String fullFilePath = defaultCsvPath + File.separator + "EmployeeData.csv";

        // Prompt the user to enter the name of the person to delete
        System.out.print("\nEnter the name to delete data: ");
        String nameToDelete = scanner.nextLine();

        // Find all rows in the CSV file matching the name
        List<String[]> matchingRows = findRowsByName(fullFilePath, nameToDelete);

        // If no matching rows are found, inform the user
        if (matchingRows.isEmpty()) {
            System.out.println("No matching data found for the name: " + nameToDelete);
        } else {
            // Display the matching rows to the user
            System.out.println("\n===== Matching Entries =====");
            for (int i = 0; i < matchingRows.size(); i++) {
                System.out.println((i + 1) + ". " + String.join(", ", matchingRows.get(i)));
            }

            // Prompt the user to select the record they want to delete
            System.out.print("\nEnter the number of the record to delete: ");
            int recordChoice = scanner.nextInt();
            scanner.nextLine();  // Clear the buffer

            // Check if the user's selection is valid
            if (recordChoice >= 1 && recordChoice <= matchingRows.size()) {
                String[] rowToDelete = matchingRows.get(recordChoice - 1);

                // Ask the user to confirm if they want to delete the selected record
                System.out.print("Are you sure you want to delete this record? (yes/no): ");
                String confirmation = scanner.nextLine();

                // If the user confirms, delete the record and reorder IDs
                if (confirmation.equalsIgnoreCase("yes")) {
                    // Delete the selected row from the CSV file
                    dbu.deleteRowByIndex(fullFilePath, rowToDelete);

                    // Reorder IDs in the CSV file
                    reorderIds(fullFilePath);

                    // Display the updated structure of the file after deletion
                    System.out.println("\n===== Updated Structure of EmployeeData.csv =====");
                    displayCsvStructure(fullFilePath);
                } else {
                    System.out.println("Record deletion cancelled.");
                }
            } else {
                System.out.println("Invalid choice. No record deleted.");
            }
        }
    }

    // Method to reorder the IDs in the CSV file after deleting a record
    private static void reorderIds(String fullFilePath) throws IOException {
        // Read all the rows from the CSV file
        List<String[]> allRows = dbu.readCsv(fullFilePath);

        // Iterate through all rows and update the ID (assumed to be in the first column)
        for (int i = 0; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            row[0] = String.valueOf(i + 1);  // Reassign the ID to be the new order (starting from 1)
        }

        // Write the updated rows back to the CSV file
        dbu.writeCsv(fullFilePath, allRows);
    }

    //=================================================================================
    //===================================Backup Part===================================
    //=================================================================================
    // Method to back up data
    private static void backupData() {
        System.out.println("\n===== Backup Data =====");
        dbu.BackupDataExtends(); // No need to pass the backup path
    }

    //=================================================================================
    //===================================Restore Part==================================
    //=================================================================================

    // Method to restore data
    private static void restoreData() {
        System.out.println("\n===== Restore Data =====");
        dbu.RestoreDataExtends(); // No need to pass the backup path
    }

    //=================================================================================
    //===================================Check csv Part================================
    //=================================================================================

    private static void checkCsvListAndStructure(Scanner scanner) throws IOException {
        String defaultCsvPath = dbu.getDefaultCsvPath();
        File folder = new File(defaultCsvPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            System.out.println("\n===== List of CSV Files =====");
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }

            boolean validChoice = false;
            while (!validChoice) {
                System.out.print("\nEnter the number of the CSV file you want to view structure: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer

                if (choice >= 1 && choice <= files.length) {
                    validChoice = true;
                    String selectedFileName = files[choice - 1].getName();
                    String fullPath = defaultCsvPath + File.separator + selectedFileName;
                    System.out.println("You selected: " + selectedFileName);

                    displayCsvStructure(fullPath); // Display structure of the selected CSV file
                    displayCsvData(fullPath); // Optionally, display data as well
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("No CSV files found in the directory.");
        }
    }


    private static void displayCsvStructure(String filePath) throws IOException {
        List<String> columns = dbu.getDataHeaders(filePath);

        if (columns.isEmpty()) {
            System.out.println("No structure found in the CSV file (no columns).");
        } else {
            System.out.println("\n===== Structure of " + filePath + " =====");
            for (String column : columns) {
                System.out.println(column);
            }
        }
    }

    private static void displayCsvData(String filePath) throws IOException {
        try {
            var data = dbu.readCsv(filePath);

            if (data != null && !data.isEmpty()) {
                System.out.println("\n===== CSV File Data =====");
                for (String[] row : data) {
                    System.out.println(String.join(", ", row));
                }
            } else {
                System.out.println("No data found in the CSV file.");
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
            throw e;
        }
    }

    //=================================================================================
    //===================================Utilities Part================================
    //=================================================================================

    // Method to view CSV file data
    public static void viewCsvFile(Scanner scanner) throws IOException {
        String defaultCsvPath = dbu.getDefaultCsvPath();
        File folder = new File(defaultCsvPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            System.out.println("\n===== List of CSV Files =====");
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }

            boolean validChoice = false;
            while (!validChoice) {
                System.out.print("\nEnter the number of the CSV file you want to view: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice >= 1 && choice <= files.length) {
                    validChoice = true;
                    String selectedFileName = files[choice - 1].getName();
                    String fullPath = defaultCsvPath + selectedFileName;
                    System.out.println("You selected: " + selectedFileName);

                    displayCsvStructure(fullPath);
                    displayCsvData(fullPath);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("No CSV files found in the directory.");
        }
    }

}

