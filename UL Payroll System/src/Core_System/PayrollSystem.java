import java.util.ArrayList;
import java.util.List;

//Payroll system class, responsible for managing employees and generating payslips
public class PayrollSystem {
    private List<Staff> staffList;
    private List<Payslip> payslips;

    //Initialisation list
    public PayrollSystem(){
        this.staffList = new ArrayList<>();
        this.payslips = new ArrayList<>();
    }

    //add new staffs
    public void addStaff(Staff staff){
        staffList.add(staff);//add staff into list
        System.out.println("Staff Added: " + staff.getName());//output confirm information
    }

    //Generate payroll for all employees
    public void generatePayslips(){
        for (Staff staff : staffList){//Iterate through the staff list
            double netPay = staff.calculateMonthlyPay();//calc net salary
            Payslip payslip = new Payslip(staff.getid(), staff.getSalaryScale(), netPay);//create payslip
            payslips.add(payslip);//add payslip to the list
            System.out.println(payslip);
        }
    }

    //promote full-time staff
    public void promoteFullTimeStaff(String id){//Iterate through the staff list
        for (Staff staff : staffList){
            if (staff instanceof FullTimeStaff && staff.getid().equals(id)){
                //Check if you are a full-time employee and match the number
                staff.promoteToNextPoint();//promote to the next point
                break;
            }
        }
    }
}
