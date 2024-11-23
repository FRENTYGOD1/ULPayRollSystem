public class FullTimeStaff extends Staff {
    private static final double HEALTH_INSURANCE = 100.0; // Health insurance costs
    private static final double UNION_FEES = 50.0;//union fee

    //constructor,Initialise Full-Time staff information
    public FullTimeStaff(String id, String name, int currentPoint, double salaryScale) {
        super(id, name, "Full-Time", salaryScale, currentPoint);
    }

    //Abstract methodology calculation of monthly salary
    @Override
    public double calculateMonthlyPay() {
        double grossPay = getSalaryScale() * getCurrentPoint();//calc gross salary
        double deductions = HEALTH_INSURANCE + UNION_FEES + calculateTax(grossPay);//calc chargebacks
        return grossPay - deductions;//Return to net salary
    }

    //Calculation of taxes
    private static double calculateTax(double grossPay) {
        //Tax bands for single people in Ireland: low rate 20%, high rate 40%
        double lowTaxRateThreshold = 40000; // €40000 is the lower tax rate limit.
        double lowTaxRate = 0.2;
        double highTaxRate = 0.4;

        //basic tax-payable
        double taxPayable = 0;

        //the part of low tax
        if (grossPay <= lowTaxRateThreshold) {
            taxPayable = grossPay * lowTaxRate;
        } else {//high tax
            //the part of low
            taxPayable = grossPay * lowTaxRateThreshold;
            //the part of high
            taxPayable += (grossPay - lowTaxRateThreshold) * highTaxRate;
        }
        return taxPayable;
    }
}
