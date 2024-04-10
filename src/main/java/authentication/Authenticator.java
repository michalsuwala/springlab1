package authentication;


import model.User;
import dao.jdbc.JdbcUserRepository;
import org.apache.commons.codec.digest.DigestUtils;


public class Authenticator {
    public static User login(String login, String password){
        JdbcUserRepository jur = JdbcUserRepository.getInstance();
        User userFromDb = jur.getUser(login);
        if ( userFromDb!= null && hashPassword(password).equals(userFromDb.getPassword())) {
            return userFromDb;
        }
        return null;
    }

    public static String hashPassword(String password){
        return DigestUtils.sha256Hex(password);
    }


}