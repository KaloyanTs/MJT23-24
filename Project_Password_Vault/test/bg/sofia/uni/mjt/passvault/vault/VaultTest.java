package bg.sofia.uni.mjt.passvault.vault;

import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotLoggedInException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.checker.PasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.password.saver.PasswordSaver;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class VaultTest {

    private final PasswordChecker checker = mock();
    private final PasswordSaver saver = mock();
    private final Vault vault = new Vault(checker, saver);

    @Test
    void testLoggingInFailsNotRegistered() {
        Response response = vault.login(new User("newUser"), Password.of("weak"));
        assertTrue(response.content().contains("not registered"),
            "Login when not registered must result in appropriate response");
        assertNull(response.password(),
            "Login when not registered must result in appropriate response");
        assertNull(response.user(),
            "Login when not registered must result in appropriate response");
    }

    @Test
    void testLoggingInFailsBadPassword() {
        vault.registerUser(new User("user1"), Password.of("user1Password"));
        Response response = vault.login(new User("user1"), Password.of("userPassword"));
        assertTrue(response.content().contains("password"),
            "Login with wrong password must result in appropriate response");
        assertNull(response.password(),
            "Login with wrong password must result in appropriate response");
        assertNull(response.user(),
            "Login with wrong password must result in appropriate response");
    }

    @Test
    void testLoggingInSuccess() {
        vault.registerUser(new User("user2"), Password.of("user1Password"));
        Response response = vault.login(new User("user2"), Password.of("user1Password"));
        assertTrue(response.content().contains("success"));
        assertNull(response.password());
        assertEquals(new User("user2"), response.user(),
            "When registered and matching password given user must be logged in");
    }

    @Test
    void testAddPassword() throws UserNotLoggedInException {
        assertThrows(UserNotLoggedInException.class, () -> vault.addPassword(new User("ownomoiod"), null, null, null));
        vault.registerUser(new User("user4"), Password.of("user1Password"));
        vault.login(new User("user4"), Password.of("user1Password"));
        Response response = vault.addPassword(new User("user4"), new Website("google"), new User("googleUser4"),
            Password.of("blablabla"));
        assertTrue(response.content().contains("added"),
            "Adding password must result in appropriate response");
    }

    @Test
    void testRemovePassword() throws UserNotLoggedInException {
        assertThrows(UserNotLoggedInException.class,
            () -> vault.removePassword(new User("ownomoiod"), null),
            "Removing password when not logged in must result in appropriate response");
        vault.registerUser(new User("user4"), Password.of("user1Password"));
        vault.login(new User("user4"), Password.of("user1Password"));
        vault.addPassword(new User("user4"), new Website("google"), new User("googleUser4"),
            Password.of(
                "blablabla"));
        Response response = vault.removePassword(new User("user4"), new Website("google"));

        assertTrue(response.content().contains("removed"),
            "Removing password successfully must result in appropriate response");
        assertNull(response.user(),
            "When removed, the username must not be returned");
        assertNull(response.password(),
            "When removed, the password must not be returned");
    }
}
