package bg.sofia.uni.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.command.RetrieveVaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.VaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotLoggedInException;
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

public class RetrieveVaultCommandTest {

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
        assertThrows(IllegalArgumentException.class, () -> new RetrieveVaultCommand(null,
            new User("dofns"), new Website("soionodad")));
    }

    @Test
    void testNoPasswordForWebsite() {
        User user = new User("Me");
        vault.registerUser(user, compromised);
        vault.login(user, compromised);
        VaultCommand command = new RetrieveVaultCommand(vault,
            new User("Me"),
            new Website("google.com")
        );
        Response response = command.execute();
        assertTrue(response.content().contains("no password stored"));
    }

    @Test
    void testNotLoggedIn() {
        User user = new User("Me");
        vault.registerUser(user, compromised);
        VaultCommand command = new RetrieveVaultCommand(vault,
            new User("Me"),
            new Website("google.com")
        );
        Response response = command.execute();
        assertTrue(response.content().contains("Not logged in"));
    }

    @Test
    void testDelegateToVault() throws UserNotLoggedInException {
        User user = new User("Me");
        vault.registerUser(user, compromised);
        vault.login(user, compromised);
        vault.addPassword(user, new Website("google.com"), user, safe);
        VaultCommand command = new RetrieveVaultCommand(vault,
            new User("Me"),
            new Website("google.com")
        );
        Response response = command.execute();
        assertFalse(response.content().contains("no password stored"));
        assertFalse(response.content().contains("Not logged in"));
    }
}
