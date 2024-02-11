package bg.sofia.uni.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.command.RemovePasswordVaultCommand;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class RemovePasswordVaultCommandTest {

    private Vault vault;
    private Password password;

    @BeforeEach
    void initVault() {
        password = Password.of("abcd");
        PasswordSaver saver = mock();
        PasswordChecker checker = mock();
        Mockito.when(checker.checkPasswordIsCompromised(password)).thenReturn(true);

        vault = new Vault(checker, saver);
    }

    @Test
    void testCreationFails() {
        assertThrows(IllegalArgumentException.class, () -> new RemovePasswordVaultCommand(
            null,
            new User("dsf"),
            new Website("sdasd")
        ));
    }

    @Test
    void testDelegateToVault() {
        User user = new User("Me");
        vault.registerUser(user, password);
        vault.login(user, password);
        VaultCommand command = new RemovePasswordVaultCommand(vault,
            new User("Me"),
            new Website("oidbi"));
        assertDoesNotThrow(command::execute);
    }

    @Test
    void testNotLoggedIn() {
        User user = new User("Me");
        vault.registerUser(user, password);
        VaultCommand command = new RemovePasswordVaultCommand(vault,
            new User("Me"),
            new Website("osduh")
        );
        Response response = command.execute();
        assertTrue(response.content().contains("Not logged in"));
    }
}
