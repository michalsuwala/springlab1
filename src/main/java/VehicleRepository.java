import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;


public class VehicleRepository implements IVehicleRepository{
    private final List<Vehicle> vehicles;
    private final String path;

    public VehicleRepository(String path) {
        this.path = path;
        this.vehicles = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if(parts.length == 7) {
                    String brand = parts[0];
                    String model = parts[1];
                    int year = Integer.parseInt(parts[2]);
                    int price = Integer.parseInt(parts[3]);
                    boolean rented = Boolean.parseBoolean(parts[4]);
                    int id = Integer.parseInt(parts[5]);
                    String category = parts[6];

                    vehicles.add(new Motorcycle(brand, model, year, price, rented, id, category));
                }
                else if(parts.length == 6) {
                    String brand = parts[0];
                    String model = parts[1];
                    int year = Integer.parseInt(parts[2]);
                    int price = Integer.parseInt(parts[3]);
                    boolean rented = Boolean.parseBoolean(parts[4]);
                    int id = Integer.parseInt(parts[5]);
                    vehicles.add(new Car(brand, model, year, price, rented, id));
                }
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rentVehicle(int id) {
        for(Vehicle v : vehicles) {
            if(v.id == id) {
                v.rented = true;
            }
        }
    }

    @Override
    public void returnVehicle(int id) {
        for(Vehicle v : vehicles) {
            if(v.id == id) {
                v.rented = false;
            }
        }
    }

    @Override
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public void save() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for(Vehicle vehicle : vehicles) {
                writer.write(vehicle.toCSV());
                writer.newLine();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
