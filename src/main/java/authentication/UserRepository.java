package authentication;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository{

    String usersPath;
    List<User> users;
    public UserRepository(String path) {
        this.usersPath = path;
        this.users = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String login = parts[0];
                String password = parts[1];
                String role = parts[2];
                int vehicleID = Integer.parseInt(parts[3]);
                users.add(new User(login, password, role, vehicleID));
            }

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public User getUser(String login) {
        for(User u : users) {
            if(u.login.equals(login)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void save() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(usersPath))) {
            for(User u : users) {
                writer.write(u.toCSV());
                writer.newLine();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
