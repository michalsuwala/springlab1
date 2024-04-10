package dao.jdbc;


import model.Motorcycle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddMotorcycleStrategy implements AddVehicleStrategy {
    private final Motorcycle motorcycle;

    public AddMotorcycleStrategy(Motorcycle motorcycle) {
        this.motorcycle = motorcycle;
    }

    @Override
    public PreparedStatement prepare(Connection conn) throws SQLException {
        String INSERT_MOTORCYCLE_SQL = "INSERT INTO tvehicle (brand, model, year, price, plate, category) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(INSERT_MOTORCYCLE_SQL);
        stmt.setString(1, motorcycle.getBrand());
        stmt.setString(2, motorcycle.getModel());
        stmt.setInt(3, motorcycle.getYear());
        stmt.setDouble(4, motorcycle.getPrice());
        stmt.setString(5, motorcycle.getPlate());
        stmt.setString(6, motorcycle.getCategory());
        return stmt;
    }
}
