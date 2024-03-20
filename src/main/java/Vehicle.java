public abstract class Vehicle {
    protected String brand;
    protected String model;
    protected int year;
    protected int price;
    protected boolean rented;
    protected int id;

    public Vehicle(String brand, String model, int year, int price, boolean rented, int id) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
        this.id = id;
    }

    public abstract String toCSV();

    @Override
    public abstract String toString();
}
