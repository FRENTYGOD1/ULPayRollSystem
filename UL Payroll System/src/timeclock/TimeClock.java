package timeclock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;

/**
 * The TimeClock class is responsible for displaying the current date, time, and day of the week
 * in a formatted manner in the terminal. It updates every second to show the real-time information.
 */
public class TimeClock {

    /**
     * The constructor of the TimeClock class. It continuously prints the current date and time
     * to the terminal, updating every second. The time is displayed in the format "yyyy-MM-dd HH:mm:ss",
     * followed by the day of the week in parentheses.
     * The program runs indefinitely until interrupted.
     */
    public void TimeClock() {
        // Set the formatter for date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Infinite loop to continuously update the displayed time
        while (true) {
            // Get the current date and time
            LocalDate currentDate = LocalDate.now();
            LocalDateTime currentDateTime = LocalDateTime.now();
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            // Mapping the days of the week to names
            String[] weekdays = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

            // Format the current date and time
            String formattedDateTime = currentDateTime.format(formatter);

            // Clear the previous output and reprint the updated time
            System.out.print("\r" + formattedDateTime + " (" + weekdays[dayOfWeek.getValue()] + ") (GMT)");

            try {
                // Wait for 1 second before updating the time again
                Thread.sleep(1000); // 1000 milliseconds (1 second)
            } catch (InterruptedException e) {
                // Print the stack trace if the thread is interrupted
                e.printStackTrace();
            }
        }
    }
}
