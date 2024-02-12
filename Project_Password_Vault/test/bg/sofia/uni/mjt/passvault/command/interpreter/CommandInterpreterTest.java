package bg.sofia.uni.mjt.passvault.command.interpreter;

import bg.sofia.uni.fmi.mjt.passvault.client.Request;
import bg.sofia.uni.fmi.mjt.passvault.command.interpreter.CommandInterpreter;
import bg.sofia.uni.fmi.mjt.passvault.password.saver.PasswordSaver;
import bg.sofia.uni.fmi.mjt.passvault.password.checker.PasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class CommandInterpreterTest {

    private final PasswordChecker checker = mock();
    private final PasswordSaver saver = mock();
    private CommandInterpreter interpreter;

    @BeforeEach
    void initVaultAndInterpreter() {
        Vault vault = new Vault(checker, saver);
        interpreter = new CommandInterpreter(vault);
    }

    @Test
    void testDisconnectNull() {
        Request request = new Request(null, "disconnect");
        assertNull(interpreter.intepretate(request).password(), "Disconnecting returns all nulls");
        assertNull(interpreter.intepretate(request).content(), "Disconnecting returns all nulls");
        assertNull(interpreter.intepretate(request).user(), "Disconnecting returns all nulls");
    }

    @Test
    void testInterpretateEmptyCommandEmptyResponse() {
        Request request = new Request(null, "");
        Response response = interpreter.intepretate(request);
        assertTrue(response.content().isEmpty(), "Empty request results in empty response");
        assertNull(response.user(), "Empty request results in empty response");
        assertNull(response.password(), "Empty request results in empty response");
    }

    @Test
    void testInterpretateBadNumberOfArguments() {
        Request request = new Request(null, "add-password");
        Response response = interpreter.intepretate(request);
        assertTrue(response.content().contains("number of arguments"),
            "Request with bad number of arguments results in appropriate message");
    }

    @Test
    void testInterpretateUnknownCommand() {
        Request request = new Request(null, "retrieve-password aisyi siabi saubiab");
        Response response = interpreter.intepretate(request);
        assertTrue(response.content().contains("Unknown"),
            "Request with unknown command results in appropriate message");
    }

    @Test
    void testInterpretateDelegateToVault() {
        Request request = new Request(null, "register Me abcd abcd");
        Response response = interpreter.intepretate(request);
        assertFalse(response.content().contains("Unknown"), "Good request causes response from vault");
        assertFalse(response.content().contains("number of arguments"), "Good request causes response from vault");
    }
}
