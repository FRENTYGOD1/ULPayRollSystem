package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * The PromotionManager class handles the promotion process for employees.
 * It reads employee data from a CSV file, checks the eligibility for promotion based on scale points,
 * and updates the employee's scale points if promotion is possible.
 */
public class PromotionManager {

    private static final String EMPLOYEE_CSV = "src/resource/csv/EmployeeData.csv"; // Replace with actual CSV path

    /**
     * Promotes an employee by their ID.
     * Reads employee details from the CSV file, checks eligibility, updates scale points, and saves the changes back to the file.
     *
     * @param employeeId The ID of the employee to promote.
     */
    public static void promoteEmployeeById(String employeeId) {
        List<String> lines = new ArrayList<>();
        boolean promoted = false;

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_CSV))) {
            String header = br.readLine(); // Read the CSV header
            lines.add(header); // Add header to updated data

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[1].trim().equals(employeeId)) { // Match employee ID
                    String name = values[0].trim();
                    String position = values[2].trim();
                    String scalePoint = values[4].trim();

                    // Check if the employee can be promoted
                    if (scalePoint.isEmpty()) {
                        System.out.println("Employee " + name + " (" + position + ") cannot be promoted. No scale points available.");
                    } else {
                        int currentScale = Integer.parseInt(scalePoint);
                        if (currentScale >= getMaxScaleForPosition(position)) {
                            System.out.println("Employee " + name + " (" + position + ") has reached the maximum scale point.");
                        } else {
                            currentScale++;
                            values[4] = String.valueOf(currentScale); // Update scale_point

                            promoted = true;
                            System.out.println("Employee " + name + " (" + position + ") has been promoted to scale point " + currentScale + ".");
                        }
                    }
                }
                lines.add(String.join(",", values)); // Add updated/unchanged row to lines
            }

            if (!promoted) {
                System.out.println("No employee found with ID: " + employeeId);
            }

            // Write the updated data back to the CSV file
            Files.write(Paths.get(EMPLOYEE_CSV), lines);
        } catch (IOException e) {
            System.out.println("Error processing CSV file: " + e.getMessage());
        }
    }

    /**
     * Determines the maximum scale point allowed for a given position.
     *
     * @param position The position of the employee.
     * @return The maximum scale point for the position.
     */
    private static int getMaxScaleForPosition(String position) {
        switch (position.toUpperCase()) {
            case "FULL_PROFESSOR(FORMERLY PROFESSOR)":
            case "PROFESSOR(FORMERLY_ASSOCIATE_PROFESSOR)":
                return 10;
            default:
                return 8;
        }
    }
}
