package enums;

public class usd {

    public static void main(String[] args) {
        double result = Currency.EUR.convertToUSD(Currency.GBP, 100);
        System.out.println("100 EUR = " + result + " GBP");
        result = Currency.USD.convertToUSD(Currency.CNY, 1000);
        System.out.println("1000 USD = " + result + " CNY");
        result = Currency.USD.convertToUSD(Currency.CHF, 1000);
        System.out.println("1000 CHF = " + result + " USD");
    }
}
enum Currency{
    USD(1.0),
    EUR(0.85),
    GBP(0.73),
    CHF(0.65),
    CNY(110.0);

    private double rateToUSD;

    Currency(double rateToUSD){
        this.rateToUSD = rateToUSD;
    }

    public double convertToUSD(Currency target, double amount) {
        double rateToUSD = amount / this.rateToUSD * target.rateToUSD;

        return rateToUSD;
    }
}