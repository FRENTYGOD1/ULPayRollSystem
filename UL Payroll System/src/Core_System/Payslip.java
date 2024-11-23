import java.time.LocalDate;

//Payroll category, which holds employee payroll information
public class Payslip {
    private String staffId;//Staff number associated with payroll
    private LocalDate date;//Payroll generation date
    private double grossPay;
    private double netPay;

    //Initialising payroll
    public Payslip(String staffId, double grossPay, double netPay){
        this.staffId = staffId;
        this.date = LocalDate.now();//Set the current date as the payroll date
        this.grossPay = grossPay;
        this.netPay = netPay;
    }

    //Exporting payroll information
    @Override
    public String toString(){
        return "Payroll{ " + "Staff ID:" + staffId + '\'' + ", Date: " + date + ", Gross salary: " + grossPay + ", Net salary: " + netPay + '}';
    }
}