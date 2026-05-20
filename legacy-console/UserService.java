import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Service layer - business logic, validation, and orchestration of DAO calls.
 */
public class UserService {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$"
    );

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user after validation.
     */
    public User registerUser(String name, String email, String password) throws SQLException {
        validateName(name);
        validateEmail(email);
        validatePassword(password);

        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        User user = new User(name.trim(), email.trim().toLowerCase(), password);
        int id = userDAO.insertUser(user);
        user.setId(id);
        return user;
    }

    /**
     * Authenticates user by email and password. Returns user without exposing password in logs.
     */
    public Optional<User> loginUser(String email, String password) throws SQLException {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Email and password are required.");
        }

        Optional<User> found = userDAO.findByEmail(email.trim().toLowerCase());
        if (found.isPresent() && found.get().getPassword().equals(password)) {
            User loggedIn = found.get();
            // Do not return password to caller in production; cleared here for demo safety
            User safe = new User(loggedIn.getId(), loggedIn.getName(), loggedIn.getEmail(), "***");
            return Optional.of(safe);
        }
        return Optional.empty();
    }

    /**
     * Updates user profile. Password is optional - pass null or blank to keep existing.
     */
    public boolean updateUser(int id, String name, String email, String newPassword) throws SQLException {
        User existing = userDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        validateName(name);
        validateEmail(email);

        String emailNormalized = email.trim().toLowerCase();
        if (!existing.getEmail().equals(emailNormalized) && userDAO.emailExists(emailNormalized)) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }

        existing.setName(name.trim());
        existing.setEmail(emailNormalized);

        if (newPassword != null && !newPassword.isBlank()) {
            validatePassword(newPassword);
            existing.setPassword(newPassword);
        }

        return userDAO.updateUser(existing);
    }

    /**
     * Deletes a user by id.
     */
    public boolean deleteUser(int id) throws SQLException {
        if (!userDAO.findById(id).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        return userDAO.deleteUser(id);
    }

    /**
     * Returns all users (admin).
     */
    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    /**
     * Validates password strength: min 8 chars, upper, lower, digit, special char.
     */
    public boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(
                    "Password must contain uppercase, lowercase, digit, and special character (@#$%^&+=!).");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}
