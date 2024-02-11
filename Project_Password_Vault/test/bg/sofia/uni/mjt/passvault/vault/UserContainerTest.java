package bg.sofia.uni.mjt.passvault.vault;

import bg.sofia.uni.fmi.mjt.passvault.exception.NoPasswordRegisteredException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.PasswordSaver;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.utility.KeyValuePair;
import bg.sofia.uni.fmi.mjt.passvault.vault.UserContainer;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class UserContainerTest {

    private final PasswordSaver saver = mock();
    private User user;
    private UserContainer container;

    @BeforeEach
    void init() {
        user = new User("Me");
        container = new UserContainer(user, saver);
    }

    @Test
    void testGetOwner() {
        assertEquals(user, container.getOwner());
    }

    @Test
    void testRetrievePasswordEquals() throws NoPasswordRegisteredException {
        Website website = new Website("facebook.com");
        Password myPass = Password.of("Pass");
        container.addPassword(website, user, myPass);
        assertEquals(new KeyValuePair<>(user, myPass), container.retrieve(website));
    }

    @Test
    void testRetrievePasswordThrowing() {
        Website website = new Website("facebook.com");
        Password myPass = Password.of("Pass");
        container.addPassword(website, user, myPass);
        assertThrows(NoPasswordRegisteredException.class, () -> container.retrieve(new Website("abv.bg")));
    }

    @Test
    void testRemovePassword() throws NoPasswordRegisteredException {
        Website website = new Website("facebook.com");
        Password myPass = Password.of("Pass");
        container.addPassword(website, user, myPass);
        assertEquals(new KeyValuePair<>(user, myPass), container.retrieve(website));
        container.removePassword(website);
        assertThrows(NoPasswordRegisteredException.class,
            () -> container.retrieve(website));

    }
}
