public class TaxCalculation {
    //Calculation of taxes
    private static double calculateTax(double grossPay){
        //Tax bands for single people in Ireland: low rate 20%, high rate 40%
        double lowTaxRateThreshold = 40000; // €40000 is the lower tax rate limit.
        double lowTaxRate = 0.2;
        double highTaxRate = 0.4;

        //basic tax-payable
        double taxPayable = 0;

        //the part of low tax
        if (grossPay <= lowTaxRateThreshold){
            taxPayable = grossPay * lowTaxRate;
        }else{//high tax
            //the part of low
            taxPayable = grossPay * lowTaxRateThreshold;
            //the part of high
            taxPayable += (grossPay - lowTaxRateThreshold) * highTaxRate;
        }
        return taxPayable;
    }
}