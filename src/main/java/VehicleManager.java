import java.util.List;
import java.util.Scanner;

public class VehicleManager {
    private final IVehicleRepository vehicleRepository;

    public VehicleManager(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void rentVehicle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vehicle id:");
        String id = scanner.nextLine();
        vehicleRepository.rentVehicle(Integer.parseInt(id));
    }

    public void returnVehicle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vehicle id:");
        String id = scanner.nextLine();
        vehicleRepository.returnVehicle(Integer.parseInt(id));
    }

    public static void main(String[] args) {
        IVehicleRepository vehicleRepository = new VehicleRepository("vehicles.txt");
        VehicleManager vehicleManager = new VehicleManager(vehicleRepository);
        Scanner scanner = new Scanner(System.in);

        List<Vehicle> vehicles;
        vehicles = vehicleRepository.getVehicles();

        while(true) {
            for(Vehicle v : vehicles) {
                System.out.println(v.toString());
            }
            System.out.println("Choose an option:");
            System.out.println("1. Rent vehicle");
            System.out.println("2. Return vehicle");
            System.out.println("3. Close");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1 -> {
                    vehicleManager.rentVehicle();
                    vehicleRepository.save();
                }
                case 2 -> {
                    vehicleManager.returnVehicle();
                    vehicleRepository.save();
                }
                case 3 -> {
                    vehicleRepository.save();
                    System.exit(0);
                }
                default -> System.out.println("Incorrect option.");
            }
        }
    }
}
