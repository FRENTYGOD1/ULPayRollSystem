import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
public class CLI {

    public static void main(String[] args){

        System.out.println("Welcome to the UL Payroll System. Please follow the prompts to proceed.");
        System.out.println("Please enter your username and password.");
        System.out.println(" ");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Account:");
        String name = scanner.nextLine();
        System.out.print("Password:");
        String passWord = scanner.nextLine();
    }

}
