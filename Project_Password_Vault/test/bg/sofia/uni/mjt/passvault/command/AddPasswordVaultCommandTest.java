package bg.sofia.uni.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.command.AddPasswordVaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.VaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.saver.PasswordSaver;
import bg.sofia.uni.fmi.mjt.passvault.password.checker.PasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class AddPasswordVaultCommandTest {

    private Vault vault;
    private Password compromised;
    private Password safe;

    @BeforeEach
    void initVault() {
        compromised = Password.of("abcd");
        safe = Password.of("ivnenr43289u9bwubysayu2");
        PasswordSaver saver = mock();
        PasswordChecker checker = mock();
        Mockito.when(checker.checkPasswordIsCompromised(compromised)).thenReturn(true);

        vault = new Vault(checker, saver);
    }

    @Test
    void testCreationFails() {
        assertThrows(IllegalArgumentException.class, () -> new AddPasswordVaultCommand(null,
            new User("odns"),
            new Website(""),
            null,
            null), "Creating command with some null argument results in an exception");
    }

    @Test
    void testCompromised() {
        User user = new User("Me");
        vault.registerUser(user, compromised);
        vault.login(user, compromised);
        VaultCommand command = new AddPasswordVaultCommand(vault,
            new User("Me"),
            new Website("google.com"),
            new User("Me"),
            compromised);
        Response response = command.execute();
        assertTrue(response.content().contains("compromised"), "Adding password first checks if it is compromised");
    }

    @Test
    void testNotLoggedIn() {
        User user = new User("Me");
        vault.registerUser(user, compromised);
        VaultCommand command = new AddPasswordVaultCommand(vault,
            new User("Me"),
            new Website("google.com"),
            new User("Me"),
            compromised);
        Response response = command.execute();
        assertTrue(response.content().contains("Not logged in"),
            "Performing command when owner not logged in results in appropriate response");
    }

    @Test
    void testDelegateToVault() {
        User user = new User("Me");
        vault.registerUser(user, compromised);
        vault.login(user, compromised);
        VaultCommand command = new AddPasswordVaultCommand(vault,
            new User("Me"),
            new Website("google.com"),
            new User("Me"),
            safe);
        Response response = command.execute();
        assertFalse(response.content().contains("compromised"),
            "When no compromised password and user logged in response comes from the vault");
        assertFalse(response.content().contains("Not logged in"),
            "When no compromised password and user logged in response comes from the vault");
    }
}
