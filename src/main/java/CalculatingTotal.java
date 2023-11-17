import java.text.DecimalFormat;
import java.util.ArrayList;

public class CalculatingTotal {


    private ArrayList<String> foodNames = new ArrayList<>();
    private ArrayList<Double> foodPrices = new ArrayList<>();
    private double total = 0;
    private double taxAmount = 0;
    private double deliveryFee = 0;
    private String name;
    private double number;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");



    // This function takes in id of the "Add to Cart" button
    // The id consists of the food name - the price of it
    // This function stores the name of the food in an array list foodNames
    // Also converts the price from string and integer and stores it
    public void calculate(String foodValueInString){

        String[] parts = foodValueInString.split("-");

        System.out.println(parts[0]);
        System.out.println(parts[1]);


        if (parts.length == 2) {
            name = parts[0]; // The name before '-'
            number = Double.parseDouble(parts[1]); // The name after '-'
        }

        foodPrices.add(number);
        foodNames.add(name);
        total += number;

    }

    // illinois tax is 6.25%
    // This method will calculate tax amount
    public double getTax(){
        taxAmount = 0.0625 * total;

        String formattedValue = decimalFormat.format(taxAmount);
        double trimmedTax = Double.parseDouble(formattedValue);

        return trimmedTax;
    }

    public double getDeliveryFee(){
        deliveryFee = 0.15 * total;

        String formattedValue = decimalFormat.format(deliveryFee);
        double trimmedDeliveryFee = Double.parseDouble(formattedValue);

        return trimmedDeliveryFee;
    }

    public double getTotal() {

        total += getTax();
        total += getDeliveryFee();

        String formattedValue = decimalFormat.format(total);
        double trimmedTotal = Double.parseDouble(formattedValue);

        return trimmedTotal;
    }

    public ArrayList<Double> getFoodPrices(){
        return foodPrices;
    }

    public ArrayList<String> getFoodNames(){
        return foodNames;
    }

    public void clearList(){
        foodNames.clear();
        foodPrices.clear();
        total = 0;
        taxAmount = 0;
        deliveryFee = 0;
        number = 0;
        name = "";
    }
}
