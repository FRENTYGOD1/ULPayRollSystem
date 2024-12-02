package database;

// This class covers all methods for database management: adding, deleting, backing up, and restoring data.

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;


public class DatabaseUtility {

    // =======================================================================
    // Member Variables Section
    // =======================================================================
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

        return data;  // Return the populated list
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
        // Start from index 1 to skip the header (assuming the first row is the header).
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            try {
                int id = Integer.parseInt(row[1]);  // Assume id is in the second column.
                maxId = Math.max(maxId, id);
            } catch (NumberFormatException e) {
                System.out.println("Error parsing ID, skipping row: " + (i + 1));
            }

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

        // Update the newData array with the generated values, according to the specified order.
        newData[1] = String.valueOf(newId);         // Set the new id.
        newData[5] = String.valueOf(newAccount);    // Set the new account.
        newData[6] = String.valueOf(newPassword);   // Set the new password.
        newData[8] = joinDate;                      // Set the join_date to the current date.

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
     * This method backs up all files and directories from a source directory to a backup directory.
     * It recursively copies all files and subdirectories from the source to the backup location.
     *
     * @param sourceDir The path of the source directory to back up.
     * @param backupDir The path of the destination directory where the backup will be stored.
     */
    public void backupFiles(String sourceDir, String backupDir) {
        try {
            // Get the File object for the source directory
            File sourceFolder = new File(sourceDir);
            // Get all files and subdirectories in the source directory
            File[] files = sourceFolder.listFiles();

            // If the source directory exists and is not empty
            if (files != null) {
                for (File file : files) {
                    // Check if it's a directory or a file
                    if (file.isDirectory()) {
                        // If it's a directory, recursively call backup
                        String newBackupDir = backupDir + File.separator + file.getName();
                        new File(newBackupDir).mkdirs(); // Create the target directory
                        backupFiles(file.getPath(), newBackupDir); // Recursively back up subdirectories
                    } else {
                        // If it's a file, perform the file copy operation
                        Path sourcePath = Paths.get(file.getPath());
                        Path targetPath = Paths.get(backupDir + File.separator + file.getName());
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING); // Copy the file
                        System.out.println("File backed up: " + file.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while backing up files.");
        }
    }

    /**
     * This method handles the backup of data. It retrieves the backup path internally and
     * performs the backup of the files from the default CSV path to the backup location.
     */
    public void BackupDataExtends() {
        // Handle backup logic internally, no need to pass the backup path
        String backupPath = getBackpath(); // Get the backup path inside the method
        System.out.println("Backing up data to: " + backupPath);
        //backup files
        backupFiles(DEFAULT_CSV_PATH, backupPath);
    }

    // =======================================================================
    // Restore Section: Methods for Restoring Data from Backup
    // =======================================================================

    /**
     * This method retrieves the backup directory path. If the backup directory does not exist,
     * it creates the directory before returning its path.
     *
     * @return The path to the backup directory.
     */
    public String getBackpath() {
        // Define the backup directory
        String backupDir = DEFAULT_CSV_PATH.replace("csv", "backup"); // Change this to your backup folder
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

    /**
     * This method restores data from a backup directory to the original directory.
     * It recursively copies files and directories from the backup to the original location.
     * If the original directory does not exist, it will be created.
     *
     * @param backupDataPathFile The backup directory containing the files and subdirectories to restore.
     * @param originalDataPathFiles The original directory where the files should be restored.
     * @throws IOException If an error occurs during the file copying process.
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
    /**
     * This method handles the logic for restoring data from a backup directory to the original directory.
     * It automatically retrieves the backup path, and then invokes the RestoreData method to perform the restoration.
     * If an error occurs during the restoration process, an error message is displayed.
     */
    public void RestoreDataExtends() {
        // Handle restore logic internally, no need to pass the backup path
        String backupPath = getBackpath(); // Get the backup path inside the method
        System.out.println("Restoring data from: " + backupPath);
        File originalDataDir = new File(DEFAULT_CSV_PATH);
        try {
            RestoreData(new File(backupPath), originalDataDir);
        } catch (IOException e) {
            System.out.println("An error occurred while restoring data.");
        }
    }

}