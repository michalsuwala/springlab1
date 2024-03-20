public class Motorcycle extends Vehicle{
    private final String category;
    public Motorcycle(String brand, String model, int year, int price, boolean rented, int id, String category) {
        super(brand, model, year, price, rented, id);
        this.category = category;
    }
    @Override
    public String toCSV() {
        return brand + ";" + model + ";" + year + ";" + price + ";" + rented + ";" + id + ";" +category;
    }

    @Override
    public String toString() {
        return "Brand: " + brand + '\n' +
                "Model: " + model + '\n' +
                "Year: " + year + '\n' +
                "Price: " + price + '\n' +
                "Rented: " + rented + '\n' +
                "ID: " + id + '\n' +
                "Category: " + category + "\n";
    }
}
