package cpsc2150.banking.models;

public class Mortgage extends AbsMortgage implements IMortgage {

    double theHomeCost;
    double theDownPayment;
    int theYears;
    ICustomer customer;
    public Mortgage(double homeCost, double downPayment, int years, ICustomer theCustomer) {

        theHomeCost = homeCost;
        theDownPayment = downPayment;
        theYears = years;
        customer = theCustomer;
    }

    public boolean loanApproved() {

        double percentDown = (theHomeCost - theDownPayment) / theHomeCost * 100;
        double debtIncomeRatio = customer.getMonthlyDebtPayments() / customer.getIncome();

        if((getRate() <= 10.0) || percentDown < 3.5 || debtIncomeRatio > 40.0) {
            return false;
        }
        else {
            return true;
        }
    }

    public double getPayment() {

        double payment = getRate() * getPrincipal() / Math.pow(1 - (1 + getRate()), -(getYears()));

        return payment;
    }

    public double getRate() {

        double percentDown = (theHomeCost - theDownPayment) / theHomeCost * 100;

        double baseAPR = 2.5;
        if(getYears() < 30) {
            baseAPR = baseAPR + 0.5;
        }
        else {
            baseAPR = baseAPR + 1.0;
        }

        if(percentDown <= 20.0) {
            baseAPR = baseAPR + 0.5;
        }

        if(customer.getCreditScore() < 500)
        {
            baseAPR = baseAPR + 10.0;
        }
        if((customer.getCreditScore() >= 500) && (customer.getCreditScore() < 600))
        {
            baseAPR = baseAPR + 5.0;
        }
        if((customer.getCreditScore() >= 600) && (customer.getCreditScore() < 700))
        {
            baseAPR = baseAPR + 1.0;
        }
        if((customer.getCreditScore() >= 700) && (customer.getCreditScore() < 750))
        {
            baseAPR = baseAPR + 0.5;
        }
        if((customer.getCreditScore() >= 750) && (customer.getCreditScore() <= 850))
        {
            baseAPR = baseAPR + 0.0;
        }

        return baseAPR;
    }

    public double getPrincipal() {

        return theHomeCost - theDownPayment;
    }

    public int getYears() {

        return theYears;
    }
}
