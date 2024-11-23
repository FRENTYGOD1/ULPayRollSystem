public class PartTimeStaff extends Staff{
    private double hourlyRate;
    private int hoursWork;

    //constructor
    public PartTimeStaff(String id, String name, double hourlyRate, int hoursWork) {
        super(id, name, "Part-Time", hourlyRate, 0); //Part-time staffs don't have a fixed pay scale and points default to 0
        this.hourlyRate = hourlyRate;
        this.hoursWork = hoursWork;
    }

    //set working times
    public void setHoursWork(int hoursWork){
        this.hoursWork = hoursWork;
    }

    //calculate salary
    @Override
    public double calculateMonthlyPay(){
        return hourlyRate * hoursWork;
    }

    @Override
    public void displayDetails(){
        super.displayDetails();
        System.out.println("Hourly Rate: " + hourlyRate);
        System.out.println("working hours: " + hoursWork);
    }
}
