package mate.academy.service.impl;

import java.util.Optional;
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
    private final UserService userService;

    @Inject
    public AuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        if (userService.findByEmail(email).isPresent()) {
            throw new RegistrationException("User with email " + email + " already exists");
        }
        byte[] salt = PasswordUtil.getSalt();
        String hashPassword = PasswordUtil.hashPassword(password, salt);
        User user = new User();
        user.setEmail(email);
        user.setSalt(salt);
        user.setPassword(hashPassword);
        return userService.add(user);
    }

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userFromDbOptional = userService.findByEmail(email);
        if (userFromDbOptional.isPresent()) {
            User user = userFromDbOptional.get();
            String hashPassword = PasswordUtil.hashPassword(password, user.getSalt());
            if (user.getPassword().equals(hashPassword)) {
                return user; // ← исправлено
            }
        }
        throw new AuthenticationException("Can't authenticate User with email " + email);
    }
}
