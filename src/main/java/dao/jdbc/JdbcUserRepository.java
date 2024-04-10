package dao.jdbc;

import authentication.Authenticator;
import dao.IUserRepository;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcUserRepository implements IUserRepository {
    private static JdbcUserRepository instance;
    private final DatabaseManager databaseManager;

    private static String GET_ALL_USERS_SQL = "SELECT login, password, role, rentedPlate FROM tuser";
    private static String GET_USER_SQL = "SELECT * FROM tuser WHERE login LIKE ?";

    private JdbcUserRepository() {
        this.databaseManager = DatabaseManager.getInstance();
    }


    @Override
    public User getUser(String login) {
        User user = null;
        Connection connection =null;
        ResultSet rs = null;
        try{
            connection = databaseManager.getConnection();
            connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_SQL);
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if(rs.next()) {
                user = new User(
                    rs.getString("login"),
                    rs.getString("password"),
                    User.Role.valueOf(rs.getString("role")),
                    rs.getString("rentedPlate")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return user;
    }

    @Override
    public void addUser(User user) {
        String INSERT_USER_SQL = "INSERT INTO tuser (login, password, role, rentedPlate) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = databaseManager.getConnection();
            stmt = conn.prepareStatement(INSERT_USER_SQL);
            stmt.setString(1, user.getLogin());
            stmt.setString(2, Authenticator.hashPassword(user.getPassword()));
            stmt.setString(3, user.getRole().toString());
            stmt.setString(4, user.getRentedPlate());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User " + user.getLogin() + " added successfully.");
            } else {
                System.out.println("Failed to add user " + user.getLogin());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    }


    @Override
    public void removeUser(String login) {
        String DELETE_USER_SQL = "DELETE FROM tuser WHERE login = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = databaseManager.getConnection();
            stmt = conn.prepareStatement(DELETE_USER_SQL);
            stmt.setString(1, login);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User " + login + " removed successfully.");
            } else {
                System.out.println("No user found with login " + login);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    }


    @Override
    public Collection<User> getUsers() {
        Collection<User> users = new ArrayList<>();
        try(Connection connection = databaseManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_SQL)) {
            while(resultSet.next()){
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                User.Role role = User.Role.valueOf(resultSet.getString("role"));
                String plate = resultSet.getString("rentedPlate");

                User user = new User(login, password, role,plate);
                users.add(user);
            }
        }catch (SQLException e){
                e.printStackTrace();
        }
        return users;
    }


    public static JdbcUserRepository getInstance(){
        if (JdbcUserRepository.instance == null){
            JdbcUserRepository.instance = new JdbcUserRepository();
        }
        return instance;
    }

}
