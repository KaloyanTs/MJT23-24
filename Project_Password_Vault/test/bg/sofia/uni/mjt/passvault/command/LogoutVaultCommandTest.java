package bg.sofia.uni.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.command.LogoutVaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.VaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.saver.PasswordSaver;
import bg.sofia.uni.fmi.mjt.passvault.password.checker.PasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class LogoutVaultCommandTest {

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
        assertThrows(IllegalArgumentException.class, () -> new LogoutVaultCommand(
            null,
            new User("dsf")
        ), "Creating command with some null argument results in an exception");
    }

    @Test
    void testDelegateToVault() {
        User user = new User("Me");
        vault.registerUser(user, password);
        vault.login(user, password);
        VaultCommand command = new LogoutVaultCommand(vault, new User("Me"));
        assertDoesNotThrow(command::execute);
    }

    @Test
    void testNotLoggedIn() {
        User user = new User("Me");
        vault.registerUser(user, password);
        VaultCommand command = new LogoutVaultCommand(vault,
            new User("Me")
        );
        Response response = command.execute();
        assertTrue(response.content().contains("Not logged in"),
            "Performing command when owner not logged in results in appropriate response");
    }
}
