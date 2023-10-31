package cpsc2150.banking.models;

public class Mortgage extends AbsMortgage implements IMortgage {

    private double theHomeCost;
    private double theDownPayment;
    private int theYears;
    ICustomer customer;
    public Mortgage(double homeCost, double downPayment, int years, ICustomer theCustomer) {

        theHomeCost = homeCost;
        theDownPayment = downPayment;
        theYears = years;
        customer = theCustomer;
    }

    private double percentDown() {

        return ((theHomeCost - theDownPayment) / theHomeCost);
    }
    public boolean loanApproved() {

        double debtIncomeRatio = customer.getMonthlyDebtPayments() / customer.getIncome();

        if((getRate() * 12) < .1 && percentDown() >= .035 && debtIncomeRatio <= .4) {
            return true;
        }
        else {
            return false;
        }
    }

    public double getPayment() {

        return ((getRate()) * getPrincipal()) / (1 - Math.pow(1 + (getRate()), -(getPayment())));

    }

    public double getRate() {

        double baseAPR = .025;

        if(theYears < 30) {
            baseAPR = baseAPR + 0.005;
        }
        else {
            baseAPR = baseAPR + .001;
        }

        if(percentDown() < .2) {
            baseAPR = baseAPR + .005;
        }

        if(customer.getCreditScore() < 500)
        {
            baseAPR = baseAPR + .1;
        }
        if((500 <= customer.getCreditScore()) && (customer.getCreditScore() < 700))
        {
            baseAPR = baseAPR + .05;
        }
        if((600 <= customer.getCreditScore()) && (customer.getCreditScore() < 700))
        {
            baseAPR = baseAPR + .01;
        }
        if((700 <= customer.getCreditScore()) && (customer.getCreditScore() < 750))
        {
            baseAPR = baseAPR + .005;
        }
        if((750 <= customer.getCreditScore()) && (customer.getCreditScore() <= 850))
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
