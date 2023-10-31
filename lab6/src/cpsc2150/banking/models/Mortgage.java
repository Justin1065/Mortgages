package cpsc2150.banking.models;

/**
 * @invariant theHomeCost >= 0 AND theDownPayment >= 0 AND MIN_YEARS <= theYears <= MAX_YEARS
 * @correspondence self.theHomeCost = theHomeCost AND self.theDownPayment = theDownPayment ABD
 *                 self.theYears = theYears AND self.customer = theCustomer
 *
 */
public class Mortgage extends AbsMortgage implements IMortgage {

    private double theHomeCost;
    private double theDownPayment;
    private int theYears;
    ICustomer customer;

    /**
     * This creates a new object to keep track information for a Mortgage instance.
     *
     * @param homeCost the cost of the customer's home
     * @param downPayment the customer's down payment for the house
     * @param years the amount of years for the loan
     * @param theCustomer an instance of the Customer object to get info about the customer
     *
     * @pre homeCost >= 0 AND downPayment >= 0 AND MIN_YEARS <= years <= MAX_YEARS
     * @post theHomeCost = homeCost AND theDownPayment = downPayment AND theYears = years AND customer = theCustomer
     */
    public Mortgage(double homeCost, double downPayment, int years, ICustomer theCustomer) {

        theHomeCost = homeCost;
        theDownPayment = downPayment;
        theYears = years;
        customer = theCustomer;
    }

    private double percentDown() {

        return ((theHomeCost - theDownPayment) / theHomeCost) * 100;
    }
    public boolean loanApproved() {

        double debtIncomeRatio = customer.getMonthlyDebtPayments()/ customer.getIncome();

        if(((getRate() * 12) < .1) && (percentDown() >= .035) && (debtIncomeRatio <= .4)) {
            return true;
        }
        else {
            return false;
        }
    }

    public double getPayment() {

        return (getRate() * getPrincipal()) / (1 - Math.pow(1 + getRate(), -getYears()));

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
