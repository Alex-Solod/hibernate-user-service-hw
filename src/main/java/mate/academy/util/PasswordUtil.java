package mate.academy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    private static final String CRYPTO_ALGORITHM = "SHA-256";

    private PasswordUtil() {
    }

    public static String getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[15];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) {
        StringBuilder hashPassword = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(CRYPTO_ALGORITHM);
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            messageDigest.update(saltBytes);
            byte[] digest = messageDigest.digest(password.getBytes());
            for (byte d : digest) {
                hashPassword.append(String.format("%02x", d));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not create hash using SHA-256 algorithm", e);
        }
        return hashPassword.toString();
    }
}
