package database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class DatabaseMenu {

    private static final DatabaseUtility dbu = new DatabaseUtility();
    public void displayDatabaseMenu(Scanner scanner) throws IOException {

        System.out.println("Displaying Database Menu...");

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
            scanner.nextLine();  // Clear the buffer

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
                    running = false;  // Set running to false to break the loop and exit
                    break;
                default:
                    System.out.println("Invalid option, please try again!");
            }
        }
    }

    /**
     * This method provides an option for the user to either add or delete an employee's data.
     * It prompts the user to choose an action and executes the corresponding operation.
     *
     * @param scanner The Scanner object used to capture user input.
     * @throws IOException If an error occurs while reading or writing to the CSV file.
     */
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
    /**
     * This method allows the user to add new data (employee information) to the "EmployeeData.csv" file.
     * It automatically generates a new ID, account, password, and join date, and then prompts the user
     * for other details like name, position, department, scale point, access level, and age.
     *
     * @param scanner The scanner object used for reading user input.
     * @throws IOException If there is an issue reading or writing to the CSV file.
     */
    private static void addData(Scanner scanner) throws IOException {
        try {
            String defaultCsvPath = dbu.getDefaultCsvPath();
            String filePath = defaultCsvPath + "/EmployeeData.csv";

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

            // Create an array to store the new data. ID, account, password, and join_date will be set later.
            String[] newData = {
                    name,
                    "",  // Placeholder for newId
                    position,
                    department,
                    scalePoint,
                    "",  // Placeholder for newAccount
                    "",  // Placeholder for newPassword
                    access,
                    "",  // Placeholder for joinDate
                    age   // Adding age at the last column
            };

            // Add the new data to the CSV
            dbu.addDataToCsvFile(filePath, newData);
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }
    }


    //=================================================================================
    //===================================Delete Part===================================
    //=================================================================================
    /**
     * This method searches for rows in the CSV file where the name in the first column
     * matches the specified name (case-insensitive).
     *
     * @param filePath The path of the CSV file to search.
     * @param nameToFind The name to search for in the first column.
     * @return A list of rows that match the given name.
     * @throws IOException If there is an issue reading the file.
     */
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

    /**
     * This method handles the deletion of an employee's record from the CSV file.
     * It allows the user to search for the employee by name, select the record to delete,
     * confirm the deletion, and then reorder the IDs in the CSV file.
     *
     * @param scanner The scanner object used to capture user input.
     * @throws IOException If there is an issue reading or writing the file.
     */
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

    /**
     * This method reorders the IDs in the specified CSV file.
     * It assumes that the IDs are located in the first column of the CSV file.
     * After reordering, the IDs are updated to reflect their new positions (starting from 1).
     *
     * @param fullFilePath The path of the CSV file whose rows' IDs need to be reordered.
     * @throws IOException If there is an issue reading or writing to the file.
     */
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
    /**
     * This method handles the process of backing up data.
     * It calls the `BackupDataExtends` method from the DatabaseUtility class
     * to perform the backup operation.
     *
     * The method displays a message indicating that the backup process is starting,
     * and then delegates the actual backup process to the `BackupDataExtends` method.
     */
    private static void backupData() {
        System.out.println("\n===== Backup Data =====");
        dbu.BackupDataExtends();
    }

    //=================================================================================
    //===================================Restore Part==================================
    //=================================================================================

    /**
     * This method handles the restoration of data from a backup.
     * It calls the `RestoreDataExtends` method from the DatabaseUtility class
     * to restore the data to its original state.
     *
     * The method displays a message indicating that the restore process is starting,
     * and delegates the actual restoration process to the `RestoreDataExtends` method.
     */
    private static void restoreData() {
        System.out.println("\n===== Restore Data =====");
        dbu.RestoreDataExtends();
    }

    //=================================================================================
    //===================================Check csv Part================================
    //=================================================================================

    /**
     * This method checks the list of CSV files in the default directory,
     * displays the available files to the user, and allows them to choose a file
     * to view its structure and data.
     *
     * It lists all CSV files found in the directory, prompts the user to select one,
     * and then displays the structure and data of the chosen file.
     *
     * @param scanner The Scanner object used to read user input.
     * @throws IOException If there is an issue reading the files or the CSV data.
     */
    private static void checkCsvListAndStructure(Scanner scanner) throws IOException {
        // Retrieve the default path where CSV files are stored
        String defaultCsvPath = dbu.getDefaultCsvPath();

        // Create a File object for the folder containing the CSV files
        File folder = new File(defaultCsvPath);

        // List all the files in the directory that end with ".csv"
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        // Check if the folder contains any CSV files
        if (files != null && files.length > 0) {
            // If CSV files exist, display them in a numbered list
            System.out.println("\n===== List of CSV Files =====");
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }

            boolean validChoice = false;
            // Keep prompting the user until a valid choice is made
            while (!validChoice) {
                System.out.print("\nEnter the number of the CSV file you want to view structure: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer

                // If the user chooses a valid file, display its structure and data
                if (choice >= 1 && choice <= files.length) {
                    validChoice = true;
                    String selectedFileName = files[choice - 1].getName();
                    String fullPath = defaultCsvPath + File.separator + selectedFileName;
                    System.out.println("You selected: " + selectedFileName);

                    // Display structure (columns) of the chosen CSV file
                    displayCsvStructure(fullPath);
                    // Optionally, display the data of the chosen CSV file
                    displayCsvData(fullPath);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            // If no CSV files are found, inform the user
            System.out.println("No CSV files found in the directory.");
        }
    }



    /**
     * This method displays the structure (columns) of the specified CSV file.
     * It retrieves the headers of the CSV file and prints each column name.
     *
     * @param filePath The path of the CSV file whose structure is to be displayed.
     * @throws IOException If there is an issue reading the file.
     */
    private static void displayCsvStructure(String filePath) throws IOException {
        // Get the headers (column names) of the CSV file
        List<String> columns = dbu.getDataHeaders(filePath);

        // If the list of columns is empty, print a message indicating no structure found.
        if (columns.isEmpty()) {
            System.out.println("No structure found in the CSV file (no columns).");
        } else {
            // Print the structure (column names) of the CSV file
            System.out.println("\n===== Structure of " + filePath + " =====");
            for (String column : columns) {
                System.out.println(column);  // Print each column name
            }
        }
    }

    /**
     * This method displays the data (rows) of the specified CSV file.
     * It prints each row of the CSV file.
     *
     * @param filePath The path of the CSV file whose data is to be displayed.
     * @throws IOException If there is an issue reading the file.
     */
    private static void displayCsvData(String filePath) throws IOException {
        try {
            // Read the data (rows) from the CSV file
            var data = dbu.readCsv(filePath);

            // Check if data is not null or empty
            if (data != null && !data.isEmpty()) {
                System.out.println("\n===== CSV File Data =====");
                // Print each row of data in the CSV file
                for (String[] row : data) {
                    System.out.println(String.join(", ", row));  // Join the elements in the row and print
                }
            } else {
                // If no data is found, print a message indicating the file is empty.
                System.out.println("No data found in the CSV file.");
            }
        } catch (IOException e) {
            // If there is an error while reading the file, print the error message and rethrow the exception.
            System.out.println("Error reading the CSV file: " + e.getMessage());
            throw e;
        }
    }

    //=================================================================================
    //===================================Utilities Part================================
    //=================================================================================
    /**
     * This method allows the user to view the structure and data of a selected CSV file.
     * It lists all the CSV files in the directory, prompts the user to select one, and
     * displays both its structure and data.
     *
     * @param scanner The scanner to read user input.
     * @throws IOException If there is an issue reading the CSV files or their data.
     */
    public static void viewCsvFile(Scanner scanner) throws IOException {
        // Get the path to the default CSV folder
        String defaultCsvPath = dbu.getDefaultCsvPath();

        // List all CSV files in the directory
        File folder = new File(defaultCsvPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        // Check if there are any CSV files in the directory
        if (files != null && files.length > 0) {
            System.out.println("\n===== List of CSV Files =====");
            // Print the list of CSV files
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }

            boolean validChoice = false;
            while (!validChoice) {
                // Prompt the user to select a CSV file by number
                System.out.print("\nEnter the number of the CSV file you want to view: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Clear the buffer

                // Check if the user's choice is valid
                if (choice >= 1 && choice <= files.length) {
                    validChoice = true;  // Mark as valid choice
                    String selectedFileName = files[choice - 1].getName();  // Get the selected file name
                    String fullPath = defaultCsvPath + selectedFileName;  // Generate the full file path
                    System.out.println("You selected: " + selectedFileName);

                    // Display the structure and data of the selected CSV file
                    displayCsvStructure(fullPath);
                    displayCsvData(fullPath);
                } else {
                    // If the user makes an invalid choice, prompt them to try again.
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            // If no CSV files are found in the directory, inform the user.
            System.out.println("No CSV files found in the directory.");
        }
    }

}

