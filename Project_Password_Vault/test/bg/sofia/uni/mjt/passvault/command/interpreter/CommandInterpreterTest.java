package bg.sofia.uni.mjt.passvault.command.interpreter;

import bg.sofia.uni.fmi.mjt.passvault.client.Request;
import bg.sofia.uni.fmi.mjt.passvault.command.interpreter.CommandInterpreter;
import bg.sofia.uni.fmi.mjt.passvault.password.PasswordSaver;
import bg.sofia.uni.fmi.mjt.passvault.password.checker.PasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class CommandInterpreterTest {

    private PasswordChecker checker = mock();
    private PasswordSaver saver = mock();
    private Vault vault;
    private CommandInterpreter interpreter;

    @BeforeEach
    void initVaultAndInterpreter() {
        vault = new Vault(checker, saver);
        interpreter = new CommandInterpreter(vault);
    }

    @Test
    void testDisconnectNull() {
        Request request = new Request(null, "disconnect");
        assertNull(interpreter.intepretate(request));
    }

    @Test
    void testEmptyCommandEmptyResponse() {
        Request request = new Request(null, "");
        Response response = interpreter.intepretate(request);
        assertTrue(response.content().isEmpty());
        assertNull(response.user());
        assertNull(response.password());
    }
    //todo add more
}
