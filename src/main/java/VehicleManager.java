import authentication.Authentication;
import authentication.User;
import authentication.UserRepository;

import java.util.List;
import java.util.Scanner;

public class VehicleManager {
    private final IVehicleRepository vehicleRepository;
    static Authentication authentication;

    public VehicleManager(IVehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        authentication = new Authentication(userRepository);
    }

    public void rentVehicle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vehicle id:");
        String id = scanner.nextLine();
        vehicleRepository.rentVehicle(Integer.parseInt(id));
        authentication.vehicleID = Integer.parseInt(id);
    }

    public void returnVehicle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vehicle id:");
        String id = scanner.nextLine();
        vehicleRepository.returnVehicle(Integer.parseInt(id));
        authentication.vehicleID = 0;
    }

    public static Vehicle addVehicleB() {
        Scanner scanner = new Scanner(System.in);
        int option;
        String brand, model, category;
        int year, price, id;

        while(true) {
            System.out.println("1. Car");
            System.out.println("2. Motorcycle");
            option = scanner.nextInt();
            if(option == 1 || option == 2) {
                break;
            }
            else {
                System.out.println("Incorrect option");
            }
        }
        scanner.nextLine();
        System.out.println("Brand:");
        brand = scanner.nextLine();
        System.out.println("Model:");
        model = scanner.nextLine();
        System.out.println("Year:");
        year = scanner.nextInt();
        System.out.println("Price:");
        price = scanner.nextInt();
        System.out.println("ID:");
        id = scanner.nextInt();
        if(option == 2) {
            System.out.println("Category:");
            category = scanner.nextLine();
            return new Motorcycle(brand, model, year, price, false, id, category);
        }
        else {
            return new Car(brand, model, year, price, false, id);
        }
    }

    public static void main(String[] args) {
        IVehicleRepository vehicleRepository = new VehicleRepository("vehicles.txt");
        UserRepository userRepository = new UserRepository("users.txt");
        List<User> userList;
        VehicleManager vehicleManager = new VehicleManager(vehicleRepository, userRepository);
        Scanner scanner = new Scanner(System.in);

        List<Vehicle> vehicles;
        vehicles = vehicleRepository.getVehicles();

        while(true) {
            System.out.println("Login:");
            String login = scanner.nextLine();
            if(authentication.authentication(login)) {
                while(true) {
                System.out.println("Password:");
                String password = scanner.nextLine();
                password = User.hashPassword(password);
                    if(password.equals(authentication.password)) {
                        //userRepository.save();
                        break;
                    } else {
                        System.out.println("Incorrect password");
                    }
                }
                break;
            }
            else {
                System.out.println("User doesn't exist");
            }

        }


        int option = 0;
        while(true) {
            if((option != 6) && ((!authentication.role.equals("client")) || (option != 4))){
                for(Vehicle v : vehicles) {
                    System.out.println(v.toString());
                }
            }
            System.out.println("Choose an option:");
            System.out.println("1. Rent vehicle");
            System.out.println("2. Return vehicle");
            System.out.println("3. Close");
            System.out.println("4. Account information");
            if(authentication.role.equals("admin")) {
                System.out.println("4. Add vehicle");
                System.out.println("5. Remove vehicle");
                System.out.println("6. User list");
            }

            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1 -> {
                    vehicleManager.rentVehicle();
                    vehicleRepository.save();
                    userRepository.getUser(authentication.login).setVehicleID(authentication.vehicleID);
                    userRepository.save();
                }
                case 2 -> {
                    vehicleManager.returnVehicle();
                    vehicleRepository.save();
                    userRepository.getUser(authentication.login).setVehicleID(authentication.vehicleID);
                    userRepository.save();
                }
                case 3 -> {
                    vehicleRepository.save();
                    System.exit(0);
                }
                case 4 -> {
                    if(authentication.role.equals("admin")) {
                        vehicleRepository.addVehicle(addVehicleB());
                        vehicleRepository.save();
                    }
                    else {
                        User user = userRepository.getUser(authentication.login);
                        System.out.println("Login: " + user.getLogin());
                        if(user.getVehicleID() == 0) {
                            System.out.println("You haven't rented any vehicle\n");
                        }
                        else {
                            System.out.println("Your rented vehicle is:");
                            for(Vehicle v : vehicles) {
                                if(v.id == user.getVehicleID()) {
                                    System.out.println(v.toString());
                                }
                            }
                        }
                    }
                }
                case 5 -> {
                    if(authentication.role.equals("admin")) {
                        System.out.println("Vehicle ID:");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        vehicleRepository.removeVehicle(id);
                        vehicleRepository.save();
                    }
                    else {
                        System.out.println("Incorrect option.");
                    }
                }
                case 6 -> {
                    if(authentication.role.equals("admin")) {
                        userList = userRepository.getUsers();
                        for(User u : userList) {
                            System.out.println("Client: " + u.getLogin() + "\nVehicle: " + u.getVehicleID());
                        }
                    } else {
                        System.out.println("Incorrect option.");
                    }
                }
                default -> System.out.println("Incorrect option.");
            }
        }
    }
}
