package dao.jdbc;

import dao.IVehicleRepository;
import model.Car;
import model.Motorcycle;
import model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcVehicleRepository implements IVehicleRepository {
    private static JdbcVehicleRepository instance;
    private final DatabaseManager manager;
    private final String GET_ALL_VEHICLE_SQL = "SELECT * FROM tvehicle";
    String GET_VEHICLE_BY_PLATE_SQL = "SELECT * FROM tvehicle WHERE plate = ?";

    private final String RENT_CAR_SQL = "UPDATE tvehicle SET rent = 1 WHERE plate LIKE ? AND rent = 0";
    private final String RETURN_CAR_SQL = "UPDATE tvehicle SET rent = 0 WHERE plate LIKE ? AND rent = 1";
    private final  String RENT_UPDATE_USER_SQL = "UPDATE tuser SET rentedplate = ? WHERE login LIKE ? AND rentedplate IS NULL";
    private final  String RETURN_UPDATE_USER_SQL = "UPDATE tuser SET rentedplate = NULL WHERE login LIKE ?";
    private final String INSERT_SQL = "INSERT INTO tvehicle (brand, model, year, price, plate) VALUES (?,?,?,?,?)";
    private final String MINSERT_SQL = "INSERT INTO tvehicle (brand, model, year, price, plate, category) VALUES (?,?,?,?,?, ?)";




    public static JdbcVehicleRepository getInstance(){
        if (JdbcVehicleRepository.instance==null){
            instance = new JdbcVehicleRepository();
        }
        return instance;
    }

    private JdbcVehicleRepository() {
        this.manager = DatabaseManager.getInstance();
    }


    @Override
    public boolean rentVehicle(String plate, String login) {
        Connection conn = null;
        PreparedStatement rentCarStmt = null;
        PreparedStatement updateUserStmt = null;

        try {
            conn = manager.getConnection();
            conn.setAutoCommit(false); // reczny commit

                                rentCarStmt = conn.prepareStatement(RENT_CAR_SQL);
                                rentCarStmt.setString(1, plate);
                                int changed1 =rentCarStmt.executeUpdate();

                                updateUserStmt = conn.prepareStatement(RENT_UPDATE_USER_SQL);
                                updateUserStmt.setString(1, plate);
                                updateUserStmt.setString(2, login);
                                int changed2 =updateUserStmt.executeUpdate();

                                if (changed1 > 0 && changed2 > 0) {
                                    System.out.println("wypozyczono");
                                    conn.commit();
                                } else {
                                    System.out.println("Nie wypożyczono");
                                    conn.rollback(); // wycofuje zmiany
                                }

        } catch(Exception e) {
            e.printStackTrace();
            if (conn!= null) {
                try {
                    conn.rollback(); // Wycofuje zmiany
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try { if (rentCarStmt != null) rentCarStmt.close(); } catch (Exception e) {};
            try { if (updateUserStmt != null) updateUserStmt.close(); } catch (Exception e) {};
            try { if (conn != null) conn.close(); } catch (Exception e) {};
        }
        return false;
    }

    @Override
    public boolean returnVehicle(String plate, String login) {
        Connection conn = null;
        PreparedStatement rentCarStmt = null;
        PreparedStatement updateUserStmt = null;

        try {
            conn = manager.getConnection();
            conn.setAutoCommit(false); // reczny commit

            rentCarStmt = conn.prepareStatement(RETURN_CAR_SQL);
            rentCarStmt.setString(1, plate);
            int changed1 =rentCarStmt.executeUpdate();

            updateUserStmt = conn.prepareStatement(RETURN_UPDATE_USER_SQL);
            updateUserStmt.setString(1, login);
            int changed2 =updateUserStmt.executeUpdate();

            if (changed1 > 0 && changed2 > 0) {
                System.out.println("Zwrócono");
                conn.commit();
            } else {
                System.out.println("Nie wypożyczono");
                conn.rollback(); // wycofuje zmiany
            }

        } catch(Exception e) {
            e.printStackTrace();
            if (conn!= null) {
                try {
                    conn.rollback(); // Wycofuje zmiany
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try { if (rentCarStmt != null) rentCarStmt.close(); } catch (Exception e) {};
            try { if (updateUserStmt != null) updateUserStmt.close(); } catch (Exception e) {};
            try { if (conn != null) conn.close(); } catch (Exception e) {};
        }
        return false;
        //return false;
    }

    public boolean addVehicle(AddVehicleStrategy strategy) {
        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = strategy.prepare(conn)
        ){

            int changed = stmt.executeUpdate();

            if (changed  > 0) {
                System.out.println("Pojazd został pomyślnie dodany.");
                return true;
            } else {
                System.out.println("Nie udało się dodać pojazdu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        try (Connection conn = manager.getConnection()) {
            if (vehicle instanceof Car) {
                return addCar((Car) vehicle, conn);
            } else if (vehicle instanceof Motorcycle) {
                return addMotorcycle((Motorcycle) vehicle, conn);
            } else {
                System.out.println("Unsupported vehicle type: " + vehicle.getClass().getSimpleName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean addCar(Car car, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setDouble(4, car.getPrice());
            stmt.setString(5, car.getPlate());

            int changed = stmt.executeUpdate();

            if (changed > 0) {
                System.out.println("Car was successfully added.");
                return true;
            } else {
                System.out.println("Failed to add car.");
            }
        }
        return false;
    }

    private boolean addMotorcycle(Motorcycle motorcycle, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(MINSERT_SQL)) {
            stmt.setString(1, motorcycle.getBrand());
            stmt.setString(2, motorcycle.getModel());
            stmt.setInt(3, motorcycle.getYear());
            stmt.setDouble(4, motorcycle.getPrice());
            stmt.setString(5, motorcycle.getPlate());
            stmt.setString(6, motorcycle.getCategory());

            int changed = stmt.executeUpdate();

            if (changed > 0) {
                System.out.println("Motorcycle was successfully added.");
                return true;
            } else {
                System.out.println("Failed to add motorcycle.");
            }
        }
        return false;
    }

    @Override
    public boolean removeVehicle(String plate) {
        String DELETE_SQL = "DELETE FROM tvehicle WHERE plate = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = manager.getConnection();
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setString(1, plate);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("model.Vehicle with plate number " + plate + " removed successfully.");
                return true;
            } else {
                System.out.println("Nie znaleziono " + plate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }



        @Override
        public Vehicle getVehicle(String plate) {
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                conn = manager.getConnection();
                stmt = conn.prepareStatement(GET_VEHICLE_BY_PLATE_SQL);
                stmt.setString(1, plate);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    String category = rs.getString("category");
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");
                    double price = rs.getDouble("price");
                    boolean rent = rs.getBoolean("rent");

                    if (category != null) {
                        return new Motorcycle(brand, model, year, price, plate, category);
                    } else {
                        return new Car(brand, model, year, price, plate);
                    }
                } else {
                    System.out.println("No vehicle found with plate number " + plate);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }



    @Override
    public Collection<Vehicle> getVehicles() {

            Collection<Vehicle> vehicles = new ArrayList<>();
            Connection connection = null;
            ResultSet rs = null;
        try {
                connection = manager.getConnection();
                Statement statement = connection.createStatement();
                rs = statement.executeQuery(GET_ALL_VEHICLE_SQL);
                while(rs.next()){
                    Vehicle v = null;
                    String category = rs.getString("category");
                    String plate = rs.getString("plate");
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");
                    double price = rs.getDouble("price");
                    boolean rent = rs.getBoolean("rent");
                    if (category != null){
                        v = new Motorcycle(brand,model,year,price,plate,category);

                    }else{
                        v = new Car(brand,model,year,price,plate);
                    }
                    v.setRent(rent);
                    vehicles.add(v);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            finally {
                if( rs!=null){try {rs.close();} catch (SQLException e) {e.printStackTrace();}}
                if( connection!=null){try {connection.close();} catch (SQLException e) {e.printStackTrace();}}
            }
            return vehicles;

        }

}
