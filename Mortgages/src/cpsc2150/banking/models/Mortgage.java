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

    private double percentDown() {

        return (theHomeCost - theDownPayment) / theHomeCost * 100;
    }
    public boolean loanApproved() {

        double debtIncomeRatio = customer.getMonthlyDebtPayments() / customer.getIncome();

        if((getRate() <= 10.0) || percentDown() < 3.5 || debtIncomeRatio > 40.0) {
            return false;
        }
        else {
            return true;
        }
    }

    public double getPayment() {

        return (getRate() * getPrincipal()) / (1 - Math.pow((1 + getRate()), -(getYears())));

    }

    public double getRate() {

        double baseAPR = 2.5;
        
        if(getYears() < 30) {
            baseAPR = baseAPR + 0.5;
        }
        else {
            baseAPR = baseAPR + 1.0;
        }

        if(percentDown() < 20.0) {
            baseAPR = baseAPR + 0.5;
        }

        if(customer.getCreditScore() < 500)
        {
            baseAPR = baseAPR + 10.0;
        }
        if((500 <= customer.getCreditScore()) && (customer.getCreditScore() < 700))
        {
            baseAPR = baseAPR + 5.0;
        }
        if((600 <= customer.getCreditScore()) && (customer.getCreditScore() < 700))
        {
            baseAPR = baseAPR + 1.0;
        }
        if((700 <= customer.getCreditScore()) && (customer.getCreditScore() < 750))
        {
            baseAPR = baseAPR + 0.5;
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
