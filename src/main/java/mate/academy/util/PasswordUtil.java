package mate.academy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {
    private static final String CRYPTO_ALGORITHM = "SHA-256";

    private PasswordUtil() {
    }

    public static byte[] getSalt() {
        // сгенерировать случайную строку/байты (String)
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[15];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) {
        // SHA-256(salt + password)
        StringBuilder hashPassword = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(CRYPTO_ALGORITHM);
            messageDigest.update(salt);
            byte[] digest = messageDigest.digest(password.getBytes());
            for (byte d : digest) {
                hashPassword.append(String.format("%02x", d));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not create hash using SHA-512 algorithm", e);
        }
        return hashPassword.toString();
    }
}
