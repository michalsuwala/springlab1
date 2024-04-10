
import authentication.Authenticator;
import dao.IUserRepository;
import dao.IVehicleRepository;
import dao.jdbc.JdbcUserRepository;
import dao.jdbc.JdbcVehicleRepository;
import model.Car;
import model.Motorcycle;
import model.User;
import model.Vehicle;

import java.util.Objects;
import java.util.Scanner;




    public class App {
        public static User user = null;
        private final Scanner scanner = new Scanner(System.in);
        private final IUserRepository iur = JdbcUserRepository.getInstance();
        private final IVehicleRepository ivr = JdbcVehicleRepository.getInstance();

        public void run() {

            System.out.println("Login:");
            String login = scanner.nextLine();
            System.out.println("Password:");
            String password = scanner.nextLine();

            user = Authenticator.login(login, password);
            if (user != null) {
                System.out.println("Loged in");

                String response = "";
                boolean running=true;
                while (running) {

                    System.out.println("-----------MENU------------");
                    System.out.println("00 - show info");
                    System.out.println("0 - show cars");
                    System.out.println("1 - rent car");
                    System.out.println("2 - return car");
                    System.out.println("6 - add car");
                    System.out.println("7 - remove car");
                    System.out.println("8 - add user");
                    System.out.println("9 - remove user");
                    response = scanner.nextLine();
                    switch (response) {
                        case "00":
                            if (Objects.equals(user.getRole(), "ADMIN")) {
                                System.out.println("List of users:");
                                for (User u : iur.getUsers()) {
                                    System.out.println(u);
                                }
                            } else {
                                System.out.println(user);
                            }
                            break;
                        case "0":
                            for (Vehicle v : ivr.getVehicles()) {
                                System.out.println(v);
                            }
                            break;
                        case "1":
                            System.out.println("plates:");
                            String plate = scanner.nextLine();
                            ivr.rentVehicle(plate,user.getLogin());
                            user = iur.getUser(user.getLogin());
                            break;
                        case "2":
                            System.out.println("plates:");
                            plate = scanner.nextLine();
                            ivr.returnVehicle(plate,user.getLogin());
                            user = iur.getUser(user.getLogin());
                            break;
                        case "6":
                            System.out.println("what do you want to add? Car/Motorcycle");
                            String type = scanner.nextLine();

                            System.out.println("Brand");
                            ////model.Motorcycle(String brand, String model, int year, double price, String plate, String category)
                            String line = scanner.nextLine();
                            String[] arr = new String[6];
                            arr[0] = line;
                            System.out.println("Model");
                            line = scanner.nextLine();
                            arr[1] = line;
                            System.out.println("Year");
                            line = scanner.nextLine();
                            arr[2] = line;
                            System.out.println("Price");
                            line = scanner.nextLine();
                            arr[3] = line;
                            System.out.println("Plate");
                            line = scanner.nextLine();
                            arr[4] = line;
                            if (type.equals("Motorcycle")) {
                                System.out.println("Category");
                                line = scanner.nextLine();
                                arr[5] = line;
                            }


                            if (type.equals("Car")) {
                                ivr.addVehicle(new Car(arr[0],
                                        arr[1],
                                        Integer.parseInt(arr[2]),
                                        Double.parseDouble(arr[3]),
                                        arr[4]));
                            }
                            else if (type.equals("Motorcycle")) {
                                System.out.println("A");
                                ivr.addVehicle(new Motorcycle(arr[0],
                                        arr[1],
                                        Integer.parseInt(arr[2]),
                                        Double.parseDouble(arr[3]),
                                        arr[4],
                                        arr[5]));
                            }
                            break;
                        case "7":
                            System.out.println("remove vehicle: ");
                            String plt = scanner.nextLine();
                            ivr.removeVehicle(plt);

                        case "8":
                            System.out.println("Add user:");
                            System.out.println("Enter login:");
                            String log = scanner.nextLine();
                            System.out.println("Enter password:");
                            String pas = scanner.nextLine();
                            System.out.println("Enter role (ADMIN/USER):");
                            String roleStr = scanner.nextLine();
                            User.Role role = User.Role.valueOf(roleStr.toUpperCase());
                            System.out.println("Enter rented plate (optional):");
                            String rentedPlate = scanner.nextLine();

                            User newUser = new User(log, pas, role, rentedPlate);
                            iur.addUser(newUser);
                            System.out.println("User added successfully.");
                            break;

                        case "9":
                            System.out.println("remove user:");
                            String  removeLogin = scanner.nextLine();

                            iur.removeUser(removeLogin);
                            break;

                        default:
                            running=false;
                    }
                }
            }else{
                System.out.println("Bledne dane!");
            }
            System.exit(0);
        }
    }

