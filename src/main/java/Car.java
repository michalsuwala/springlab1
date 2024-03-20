public class Car extends Vehicle {
    public Car(String brand, String model, int year, int price, boolean rented, int id) {
        super(brand, model, year, price, rented, id);
    }
    @Override
    public String toCSV() {
        return brand + ";" + model + ";" + year + ";" + price + ";" + rented + ";" + id;
    }

    @Override
    public String toString() {
        return "Brand: " + brand + '\n' +
                "Model: " + model + '\n' +
                "Year: " + year + '\n' +
                "Price: " + price + '\n' +
                "Rented: " + rented + '\n' +
                "ID: " + id + '\n';
    }
}
