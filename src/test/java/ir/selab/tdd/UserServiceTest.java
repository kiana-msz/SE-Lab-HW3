package ir.selab.tdd;

import ir.selab.tdd.domain.User;
import ir.selab.tdd.repository.UserRepository;
import ir.selab.tdd.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest {
    private UserService userService;

    @Before
    public void setUp() {
        UserRepository userRepository = new UserRepository(List.of());
        userService = new UserService(userRepository);
        userService.registerUser("admin", "1234");
        userService.registerUser("ali", "qwert");
    }

    @Test
    public void createNewValidUser__ShouldSuccess() {
        String username = "reza";
        String password = "123abc";
        boolean b = userService.registerUser(username, password);
        assertTrue(b);
    }

    @Test
    public void createNewDuplicateUser__ShouldFail() {
        String username = "ali";
        String password = "123abc";
        boolean b = userService.registerUser(username, password);
        assertFalse(b);
    }

    @Test
    public void loginWithValidUsernameAndPassword__ShouldSuccess() {
        boolean login = userService.loginWithUsername("admin", "1234");
        assertTrue(login);
    }

    @Test
    public void loginWithValidUsernameAndInvalidPassword__ShouldFail() {
        boolean login = userService.loginWithUsername("admin", "abcd");
        assertFalse(login);
    }

    @Test
    public void loginWithInvalidUsernameAndInvalidPassword__ShouldFail() {
        boolean login = userService.loginWithUsername("ahmad", "abcd");
        assertFalse(login);
    }

    @Test
    public void shouldLoginWithEmail() {
        User user = new User("user1", "password1", "user1@example.com");
        UserRepository repository = new UserRepository(List.of(user));
        UserService service = new UserService(repository);

        assertTrue(service.loginWithEmail("user1@example.com", "password1"));
        assertFalse(service.loginWithEmail("user1@example.com", "wrongpassword"));
        assertFalse(service.loginWithEmail("nonexistent@example.com", "password1"));
    }

    @Test
    public void shouldRemoveUserByUsername() { 
        User user = new User("user1", "password1", "user1@example.com");
        UserRepository repository = new UserRepository(List.of(user));
        UserService service = new UserService(repository);

        assertTrue(service.removeUser("user1"));
        assertNull(repository.getUserByUsername("user1"));
    }

    @Test
    public void shouldReturnAllUsers() {
        User user1 = new User("user1", "password1", "user1@example.com");
        User user2 = new User("user2", "password2", "user2@example.com");
        UserRepository repository = new UserRepository(List.of(user1, user2));
        UserService service = new UserService(repository);

        assertEquals(2, service.getAllUsers().size());
    }

    @Test
    public void shouldChangeUserEmail() {
        User user = new User("user1", "password1", "old@example.com");
        UserRepository repository = new UserRepository(List.of(user));
        UserService service = new UserService(repository);

        assertTrue(service.changeUserEmail("user1", "new@example.com"));
        assertEquals(user, repository.getUserByEmail("new@example.com"));
        assertNull(repository.getUserByEmail("old@example.com"));
    }

    @Test
    public void shouldNotChangeToDuplicateEmail() {
        User user1 = new User("user1", "password1", "user1@example.com");
        User user2 = new User("user2", "password2", "user2@example.com");
        UserRepository repository = new UserRepository(List.of(user1, user2));
        UserService service = new UserService(repository);

        assertFalse(service.changeUserEmail("user1", "user2@example.com"));
    }

    @Test
    public void shouldRegisterUserWithEmailSuccessfully() {
        String username = "newUser";
        String password = "password123";
        String email = "newuser@example.com";

        boolean result = userService.registerUser(username, password, email);

        assertTrue(result);
        assertEquals(4, userService.getAllUsers().size());
        assertNotNull(userService.getAllUsers().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null));
    }

    @Test
    public void shouldNotRegisterUserWithDuplicateEmail() {
        String username = "user1";
        String password = "password1";
        String email = "admin@example.com"; // Duplicate email

        boolean result = userService.registerUser(username, password, email);

        assertFalse(result);
    }

    @Test
    public void shouldNotRegisterUserWithDuplicateUsername() {
        String username = "admin"; // Duplicate username
        String password = "password1";
        String email = "unique@example.com";

        boolean result = userService.registerUser(username, password, email);

        assertFalse(result);
    }
}
