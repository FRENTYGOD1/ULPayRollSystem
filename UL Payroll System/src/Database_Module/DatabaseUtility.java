package Database_Module;

// This class covers all methods for database management: adding, deleting, backing up, and restoring data.

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class DatabaseUtility {

    // =======================================================================
    // Member Variables Section
    // =======================================================================
    private List<String> data; // List to store data
    private DatabaseUtility DBU; // Helper for recursive calls in backup and restore
    private String line; // Line variable for reading CSV content
    private static final String DEFAULT_CSV_PATH = "src/resource/csv/";


    // =======================================================================
    // CsvReader Section: Methods for Reading CSV Files
    // =======================================================================

    /**
     * This method writes data to a CSV file.
     *
     * @param filePath The path where the CSV file will be written.
     * @param data The data to be written, represented as a list of string arrays.
     * @throws IOException If an error occurs while writing to the file.
     */

    // Reads a CSV file and returns a list of string arrays (each array corresponds to a line in the CSV)
    public void writeCsv(String filePath, List<String[]> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the data and write each row to the file
            for (String[] row : data) {
                writer.write(String.join(",", row)); // Join the columns with commas
                writer.newLine(); // Write a new line after each row
            }
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
            throw e; // Rethrow the exception
        }
    }

    //Set default path for csv
    public String getDefaultCsvPath() {
        return DEFAULT_CSV_PATH;  // Return the path to the default CSV directory
    }
    public List<String[]> readCsv(String filePath) throws IOException{
        List<String[]> data = new ArrayList<>();  // Initialize the list to hold CSV data

        // Check if the file path is valid
        if (filePath == null || filePath.trim().isEmpty()) {
            filePath = DEFAULT_CSV_PATH;  // Default path
            System.err.println("Invalid file path: " + filePath);
            return data;  // Return empty list
        }

        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return data;  // Return empty list if the file does not exist
        }

        // Try to read the CSV file using BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");  // Split by commas
                data.add(values);  // Add the parsed line to the data list
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return data;  // Return the populated list (could be empty)
    }



    // =======================================================================
    // DataEditor Section: Methods for Adding and Deleting Data
    // =======================================================================

    // ============================== Add Data Section ==============================

    /**
     * This method adds a new data entry to the CSV file.
     * It automatically generates unique values for id, account, password, and join_date.
     *
     * @param filePath The path to the CSV file where data will be added.
     * @param newData The new data to be added (as an array of strings).
     * @throws IOException If an error occurs while adding the data.
     */
    public void addDataToCsvFile(String filePath, String[] newData) throws IOException {
        // Get the current data from the CSV file.
        List<String[]> data = readCsv(filePath);

        // Initialize variables to store the maximum id, account, and password values.
        int maxId = 0;
        int maxAccount = 0;
        int maxPassword = 0;

        // Loop through the existing data to find the maximum id, account, and password values.
        for (String[] row : data) {
            int id = Integer.parseInt(row[1]);  // Assume id is in the second column.
            maxId = Math.max(maxId, id);

            // Try parsing account and password, assuming they're integers (handle exceptions if not).
            try {
                int account = Integer.parseInt(row[5]);  // Assume account is in the sixth column.
                int password = Integer.parseInt(row[6]);  // Assume password is in the seventh column.
                maxAccount = Math.max(maxAccount, account);
                maxPassword = Math.max(maxPassword, password);
            } catch (NumberFormatException e) {
                System.out.println("Account and Password are not integers, skipping increment.");
            }
        }

        // Generate new unique values for id, account, and password.
        int newId = maxId + 1;
        int newAccount = maxAccount + 1;
        int newPassword = maxPassword + 1;
        String joinDate = LocalDate.now().toString();  // Get the current date in YYYY-MM-DD format.

        // Update the newData array with the generated values.
        newData[1] = String.valueOf(newId);  // Set the new id.
        newData[5] = String.valueOf(newAccount);  // Set the new account.
        newData[6] = String.valueOf(newPassword);  // Set the new password.
        newData[8] = joinDate;  // Set the join_date to the current date.

        // Add the new data to the list.
        data.add(newData);

        // Write the updated data back to the CSV file.
        writeCsv(filePath, data);
        System.out.println("Data added successfully!");
    }

    // ============================== Delete Data Section ==============================

    /**
     * This method deletes a row from the CSV file based on the provided data (matching row).
     *
     * @param filePath The path to the CSV file.
     * @param rowToDelete The row to be deleted (as an array of strings).
     * @throws IOException If an error occurs while deleting the data.
     */
    public void deleteRowByIndex(String filePath, String[] rowToDelete) throws IOException {
        // Read the data from the CSV file.
        List<String[]> data = readCsv(filePath);

        // Remove the row that matches the provided rowToDelete.
        data.removeIf(row -> Arrays.equals(row, rowToDelete));

        // Write the updated data back to the CSV file.
        writeCsv(filePath, data);
    }



    // =======================================================================
    // CSV Header Reading Section: Fetching Column Headers
    // =======================================================================

    /**
     * This method retrieves and prints the headers from a CSV file.
     *
     * @param filePath The path to the CSV file.
     * @return A list of column headers.
     * @throws IOException If an error occurs while reading the file.
     */

    public List<String> getDataHeaders(String filePath) throws IOException {
        List<String> headers = new ArrayList<>();

        // Read the first row (header) of the CSV file.
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();  // Read the first line of the file (header).
            if (line != null) {
                String[] columns = line.split(",");  // Split the row by commas to get columns.
                headers.addAll(Arrays.asList(columns));  // Add the columns to the list.
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error reading the CSV file.");
        }

        return headers;  // Return the list of column headers.
    }

    // =======================================================================
    // Backup Section: Methods for Backing Up Data
    // =======================================================================

    /**
     * This method backs up data from the original directory to the destination directory.
     *
     * @param originalDataPathFile The original directory containing the data to be backed up.
     * @param destinationDataPathFiles The destination directory where the backup will be saved.
     * @throws IOException If an error occurs during the backup process.
     */

    public static void BackupData(File originalDataPathFile, File destinationDataPathFiles) throws IOException {
        // Check if the destination folder exists, if not create it
        if (!destinationDataPathFiles.exists()) {
            destinationDataPathFiles.mkdirs();  // Use mkdirs() to create the directory
        }

        // Get all files and subdirectories in the source directory
        File[] files = originalDataPathFile.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // If it's a directory, call BackupData recursively to copy the subdirectory
                    BackupData(file, new File(destinationDataPathFiles, file.getName()));
                } else {
                    // If it's a file, copy the file to the destination directory
                    Files.copy(file.toPath(), new File(destinationDataPathFiles, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    // Extends the BackupData method, calling it with predefined source and destination paths
    public void BackupDataExtends() {
        // Handle backup logic internally, no need to pass the backup path
        String backupPath = getBackpath(); // Get the backup path inside the method
        System.out.println("Backing up data to: " + backupPath);
        // Perform backup logic...
    }

    // =======================================================================
    // Restore Section: Methods for Restoring Data from Backup
    // =======================================================================

    /**
     * This method restores data from a backup directory to the original directory.
     *
     * @param backupDataPathFile The destination directory containing the backup.
     * @param originalDataPathFiles The original directory where the data will be restored.
     * @throws IOException If an error occurs during the restore process.
     */
    public static void RestoreData(File backupDataPathFile, File originalDataPathFiles) throws IOException {

        // Check if the original data path exists, if not create it
        if (!originalDataPathFiles.exists()) {
            originalDataPathFiles.mkdirs();  // Use mkdirs() to create the directory
        }

        // Get all files and subdirectories in the backup directory
        File[] files = backupDataPathFile.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {

                    // If it's a directory, call RestoreData recursively to restore the subdirectory
                    RestoreData(file, new File(originalDataPathFiles, file.getName()));
                } else {

                    // If it's a file, restore the file to the original directory
                    Files.copy(file.toPath(), new File(originalDataPathFiles, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
    // Method to get the path where backup files are stored
    public String getBackpath() {
        // Define the backup directory (you can customize this path)
        String backupDir = "/path/to/your/backup/directory"; // Change this to your backup folder
        File folder = new File(backupDir);

        // If the backup folder doesn't exist, create it
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("Backup directory created: " + backupDir);
            } else {
                System.out.println("Failed to create backup directory.");
            }
        }

        // Return the path of the backup folder
        return backupDir;
    }

    // Extends the RestoreData method, calling it with predefined backup and original paths
    public void RestoreDataExtends() {
        // Handle restore logic internally, no need to pass the backup path
        String backupPath = getBackpath(); // Get the backup path inside the method
        System.out.println("Restoring data from: " + backupPath);
        // Perform restore logic...
    }

}
