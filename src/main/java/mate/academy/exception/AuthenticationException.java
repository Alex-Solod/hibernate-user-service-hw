package mate.academy.exception;

import mate.academy.model.User;

public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public interface AuthenticationService {
        User login(String email, String password) throws AuthenticationException;
    }
}
