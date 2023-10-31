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
    private double apr;

    private double principle;
    private double monthlyPayment;
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

        apr = BASERATE;

        if(theYears < MAX_YEARS) {
            apr = apr + GOODRATEADD;
        }
        else {
            apr = apr + NORMALRATEADD;
        }

        if(percentDown() < PREFERRED_PERCENT_DOWN) {
            apr = apr + GOODRATEADD;
        }

        if(customer.getCreditScore() < BADCREDIT)
        {
            apr = apr + VERYBADRATEADD;
        }
        if((BADCREDIT <= customer.getCreditScore()) && (customer.getCreditScore() < FAIRCREDIT))
        {
            apr = apr + BADRATEADD;
        }
        if((FAIRCREDIT <= customer.getCreditScore()) && (customer.getCreditScore() < GOODCREDIT))
        {
            apr = apr + NORMALRATEADD;
        }
        if((GOODCREDIT <= customer.getCreditScore()) && (customer.getCreditScore() < GREATCREDIT))
        {
            apr = apr + GOODRATEADD;
        }

        principle = theHomeCost - theDownPayment;

        double numPayments = MONTHS_IN_YEAR * years;
        monthlyPayment = (apr * principle) / (1 - Math.pow(1 + apr, -numPayments));
    }

    private double percentDown() {

        return ((theDownPayment) / theHomeCost) * 100;
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

        return monthlyPayment;

    }

    public double getRate() {

        return apr;
    }

    public double getPrincipal() {

        return principle;
    }

    public int getYears() {

        return theYears;
    }
}
