package authentication;

import org.apache.commons.codec.digest.DigestUtils;

public class User {
    String login;
    String password;
    String role;
    int vehicleID;

    public User(String login, String password, String role, int vehicleID) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.vehicleID = vehicleID;
    }

    public static String hashPassword(String password){
        return DigestUtils.sha256Hex(password);
    }

    public String getLogin() {
        return login;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    String toCSV() {
        return login + ";" + password + ";" + role + ";" + vehicleID;
    }

    /*String toCSV() {
        return login + ";" + hashPassword(password) + ";" + role + ";" + vehicleID;
    }*/
}
