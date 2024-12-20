package ir.selab.tdd;

import ir.selab.tdd.domain.User;
import ir.selab.tdd.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {
    private UserRepository repository;

    @Before
    public void setUp() {
        List<User> userList = Arrays.asList(
                new User("admin", "1234"),
                new User("ali", "qwert"),
                new User("mohammad", "123asd"));
        repository = new UserRepository(userList);
    }

    @Test
    public void getContainingUser__ShouldReturn() {
        User ali = repository.getUserByUsername("ali");
        assertNotNull(ali);
        assertEquals("ali", ali.getUsername());
        assertEquals("qwert", ali.getPassword());
    }

    @Test
    public void getNotContainingUser__ShouldReturnNull() {
        User user = repository.getUserByUsername("reza");
        assertNull(user);
    }

    @Test
    public void createRepositoryWithDuplicateUsers__ShouldThrowException() {
        User user1 = new User("ali", "1234");
        User user2 = new User("ali", "4567");
        assertThrows(IllegalArgumentException.class, () -> {
            new UserRepository(List.of(user1, user2));
        });
    }

    @Test
    public void addNewUser__ShouldIncreaseUserCount() {
        int oldUserCount = repository.getUserCount();

        // Given
        String username = "reza";
        String password = "123abc";
        String email = "reza@sharif.edu";
        User newUser = new User(username, password);

        // When
        repository.addUser(newUser);

        // Then
        assertEquals(oldUserCount + 1, repository.getUserCount());
    }

    @Test
    public void shouldPopulateUsersByEmail() {
        User user1 = new User("user1", "password1", "user1@example.com");
        User user2 = new User("user2", "password2", "user2@example.com");
        UserRepository repository = new UserRepository(List.of(user1, user2));

        assertEquals(user1, repository.getUserByEmail("user1@example.com"));
        assertEquals(user2, repository.getUserByEmail("user2@example.com"));
    }

    @Test
    public void shouldReturnUserByEmail() {
        User user = new User("user1", "password1", "user1@example.com");
        UserRepository repository = new UserRepository(List.of(user));

        assertEquals(user, repository.getUserByEmail("user1@example.com"));
    }

    @Test
    public void shouldReturnNullIfEmailNotFound() {
        UserRepository repository = new UserRepository(List.of());
        assertNull(repository.getUserByEmail("nonexistent@example.com"));
    }

    @Test
    public void shouldNotAddUserWithDuplicateEmail() {
        UserRepository repository = new UserRepository(List.of());

        User user1 = new User("user1", "password1", "user@example.com");
        User user2 = new User("user2", "password2", "user@example.com");

        assertTrue(repository.addUser(user1));
        assertFalse(repository.addUser(user2));
    }

    @Test
    public void shouldRemoveUserByUsername() {
        User user = new User("user1", "password1", "user1@example.com");
        UserRepository repository = new UserRepository(List.of(user));

        assertTrue(repository.removeUser("user1"));
        assertNull(repository.getUserByUsername("user1"));
    }

     @Test
    public void shouldReturnFalseWhenRemovingNonExistentUser() {
        UserRepository repository = new UserRepository(List.of());
        assertFalse(repository.removeUser("nonexistent"));
    }
}
