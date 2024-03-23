package authentication;

import java.util.List;

public class Authentication {
    UserRepository userRepository;
    public String login;
    public String password;
    public String role;
    public int vehicleID;

    public Authentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authentication(String login) {
        for(User u : userRepository.users) {
            if(u.login.equals(login)) {
                this.login = login;
                this.password = u.password;
                this.role = u.role;
                this.vehicleID = u.vehicleID;
                return true;
            }
        }
        return false;
    }
}
