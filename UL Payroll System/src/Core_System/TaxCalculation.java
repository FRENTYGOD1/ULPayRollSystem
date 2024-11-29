package utils;

public class TaxCalculation {
    private double taxPayable;//basic tax-payable

    //Calculation of taxes
    public double calculateTax(double grossPay){
        //Tax bands for single people in Ireland: low rate 20%, high rate 40%
        double lowTaxRateThreshold = 42000;// €42000 is the lower tax rate limit.
        double lowTaxRate = 0.2;
        double highTaxRate = 0.4;

        //basic tax-payable


        //the part of low tax
        if (grossPay <= lowTaxRateThreshold){
            taxPayable = grossPay * lowTaxRate;
        }else{//high tax
            //the part of low
            taxPayable += lowTaxRateThreshold * lowTaxRate;
            //the part of high
            taxPayable += (grossPay - lowTaxRateThreshold) * highTaxRate;
        }
        taxPayable /= 12;//monthly tax
        return taxPayable;
    }

    public double getTaxPayable(){
        return taxPayable;
    }
}
