package cpsc2150.banking.models;

/**
 * @invariant theHomeCost >= 0 AND theDownPayment >= 0 AND MIN_YEARS <= theYears <= MAX_YEARS AND apr > BASERATE AND
 *            principle > 0 AND monthlyPayment > 0 AND 0 <= PercentDown < 1 AND debtIncomeRatio > 0 AND numPayments > 0
 *
 * @correspondence Payment = monthlyPayment AND Rate = apr AND Customer = customer AND DebtToIncomeRatio =
 *                 debtIncomeRatio AND Principal = principal AND NumberOfPayments = numPayments AND PercentDown =
 *                 percentDown
 *
 */
public class Mortgage extends AbsMortgage implements IMortgage {

    private double theHomeCost;
    private double theDownPayment;
    private int theYears;
    private double apr;
    private double principal;
    private double monthlyPayment;
    private double percentDown;
    private double debtIncomeRatio;

    private double numPayments;
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
     *       AND percentDown = theDownPayment/theHomeCost AND apr = [(BASERATE + rates that match the customer's
     *       background information)/the months in the year] AND principle = theHomeCost - theDownPayment AND
     *       monthlyPayment = [(apr * principle) / (1 - (1 + apr) raised to the negative numPayments))] AND
     *       debtIncomeRatio = [(monthlyPayment * the months in the year)/ customer's income]
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

        //dividing the apr to show the apr per month in a year
        apr = apr/MONTHS_IN_YEAR;

        // Calculates the principle based on the formula given
        principal = theHomeCost - theDownPayment;

        // Calculates the number of payments made by the customer using the months in the years and the years passed in
        numPayments = MONTHS_IN_YEAR * years;

        // Calculates the monthly payment using the apr, principle, and number of payments with the formula given
        monthlyPayment = (apr * principal) / (1 - Math.pow((1 + apr), (-numPayments)));

        // Calculates the debt income ratio using the monthly payments, months in the year, and the customer's income
        // with the formula given
        debtIncomeRatio = (monthlyPayment * MONTHS_IN_YEAR)/ customer.getIncome();
    }

    // Returns the percentDown
    private double percentDown() {

        return percentDown;
    }
    public boolean loanApproved() {

        // If conditional that determines whether the customer can get a loan or not
        //The loan is not rejected if the apr is less than the cap rate, if the percent down is greater than the min
        //percent down required, and if the debt to income ratio is less than or equal than the cap ratio
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

        return principal;
    }

    // Returns the years
    public int getYears() {

        return theYears;
    }
}
