import java.util.List;

public interface IVehicleRepository {
    void rentVehicle(int id);
    void returnVehicle(int id);
    void addVehicle(Vehicle vehicle);
    void removeVehicle(int id);
    List<Vehicle> getVehicles();
    void save();
}
