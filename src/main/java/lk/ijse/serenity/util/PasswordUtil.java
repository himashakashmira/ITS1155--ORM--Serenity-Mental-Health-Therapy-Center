package lk.ijse.serenity.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // Password encrypt
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Password check (Login)
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}