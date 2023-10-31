package cpsc2150.banking.models;

/**
 * @invariant theHomeCost >= 0 AND theDownPayment >= 0 AND MIN_YEARS <= theYears <= MAX_YEARS AND apr = BASERATE AND
 *            principle > 0 AND monthlyPayment > 0 AND 0 <= PercentDown < 1 AND debtIncomeRatio > 0
 * @correspondence self.theHomeCost = theHomeCost AND self.theDownPayment = theDownPayment AND
 *                 self.theYears = theYears AND self.apr = apr AND
 *                 self.principle = principle AND self.monthlyPayments = monthlyPayments AND
 *                 self.percentDown = percentDown AND self.debtIncomeRatio = debtIncomeRatio AND self.customer = theCustomer
 *
 */
public class Mortgage extends AbsMortgage implements IMortgage {

    private double theHomeCost;
    private double theDownPayment;
    private int theYears;
    private double apr;

    private double principle;
    private double monthlyPayment;
    private double percentDown;
    private double debtIncomeRatio;
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

        // Calculates the percent down
        percentDown = theDownPayment / theHomeCost;

        // Sets the apr to the base apr
        apr = BASERATE;

        // If condition that uses the years passed in to determine how much to add to the apr
        if(theYears < MAX_YEARS) {
            apr = apr + GOODRATEADD;
        }
        else {
            apr = apr + NORMALRATEADD;
        }

        // If condition that uses the percent down to determine how much to add to the apr
        if(percentDown() < PREFERRED_PERCENT_DOWN) {
            apr = apr + GOODRATEADD;
        }

        // If conditions that determine how much to add to the apr based on the customer credit score
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

        apr = apr/MONTHS_IN_YEAR;

        // Calculates the principle
        principle = theHomeCost - theDownPayment;

        // Calculates the number of payments made by the customer using the months in the years and the years passed in
        double numPayments = MONTHS_IN_YEAR * years;

        // Calculates the monthly payment using the apr, principle, and number of payments
        monthlyPayment = (apr * principle) / (1 - Math.pow((1 + apr), (-numPayments)));

        debtIncomeRatio = (monthlyPayment * MONTHS_IN_YEAR)/ customer.getIncome();
    }

    // Returns the percentDown
    private double percentDown() {

        return percentDown;
    }
    public boolean loanApproved() {

        // If conditional that determines whether the customer can get a loan or not
        if(((apr * MONTHS_IN_YEAR) < RATETOOHIGH) && (percentDown() >= MIN_PERCENT_DOWN) && (debtIncomeRatio <= DTOITOOHIGH)) {
            return true;
        }
        else {
            return false;
        }
    }

    // Returns the monthly payment
    public double getPayment() {

        return monthlyPayment;

    }

    // Returns the apr
    public double getRate() {

        return apr * MONTHS_IN_YEAR;
    }

    // Returns the principle
    public double getPrincipal() {

        return principle;
    }

    // Returns the years
    public int getYears() {

        return theYears;
    }
}
