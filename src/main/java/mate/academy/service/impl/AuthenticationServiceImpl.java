package mate.academy.service.impl;

import java.util.Optional;
import java.util.regex.Pattern;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.PasswordUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private final UserService userService;

    @Inject
    public AuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        System.out.println("INFO: Attempting to register user with email: " + email);
        
        if (email == null || email.trim().isEmpty()) {
            System.err.println("ERROR: Registration failed - email is null or empty");
            throw new RegistrationException("Email cannot be null or empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.err.println("ERROR: Registration failed - password is null or empty for email: " 
                    + email);
            throw new RegistrationException("Password cannot be null or empty");
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            System.err.println("ERROR: Registration failed - invalid email format: " + email);
            throw new RegistrationException("Invalid email format: " + email);
        }
        
        if (password.trim().length() < 6) {
            System.err.println("ERROR: Registration failed - password too short for email: " 
                    + email);
            throw new RegistrationException("Password must be at least 6 characters long");
        }
        
        if (userService.findByEmail(email.trim()).isPresent()) {
            System.err.println("ERROR: Registration failed - email already exists: " + email);
            throw new RegistrationException("User with email " + email + " already exists");
        }
        
        User user = new User();
        user.setEmail(email.trim());
        user.setPassword(password);
        
        System.out.println("INFO: User registration successful: " + email);
        return userService.add(user);
    }

    @Override
    public User login(String email, String password) throws AuthenticationException {
        System.out.println("INFO: Attempting login for email: " + email);
        
        if (email == null || email.trim().isEmpty()) {
            System.err.println("ERROR: Login failed - email is null or empty");
            throw new AuthenticationException("Email cannot be null or empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.err.println("ERROR: Login failed - password is null or empty for email: " 
                    + email);
            throw new AuthenticationException("Password cannot be null or empty");
        }
        
        Optional<User> userFromDbOptional = userService.findByEmail(email.trim());
        if (userFromDbOptional.isPresent()) {
            User user = userFromDbOptional.get();
            String hashPassword = PasswordUtil.hashPassword(password, user.getSalt());
            if (user.getPassword().equals(hashPassword)) {
                System.out.println("INFO: Login successful for email: " + email);
                return user;
            } else {
                System.err.println("ERROR: Login failed - incorrect password for email: " + email);
            }
        } else {
            System.err.println("ERROR: Login failed - user not found with email: " + email);
        }
        
        throw new AuthenticationException("Can't authenticate User with email " + email);
    }
}
