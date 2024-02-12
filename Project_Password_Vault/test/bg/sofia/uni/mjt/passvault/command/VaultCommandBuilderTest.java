package bg.sofia.uni.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.command.AddPasswordVaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.GeneratePaswordVaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.LogoutVaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.RegisterVaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.RetrieveVaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.VaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.VaultCommandBuilder;
import bg.sofia.uni.fmi.mjt.passvault.exception.BadCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.passvault.exception.NoCommandTypeGivenException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VaultCommandBuilderTest {

    private VaultCommandBuilder builder;

    @BeforeEach
    void initBuilder() {
        Vault vault = new Vault(null, null);
        builder = new VaultCommandBuilder(vault);
    }

    @Test
    void testNullVaultThrows() {
        assertThrows(IllegalArgumentException.class, () -> new VaultCommandBuilder(null),
            "VaultCommandBuilder must be injected with a valid Vault");
    }

    @Test
    void testBuildNoTypeFails() {
        assertThrows(NoCommandTypeGivenException.class, () -> builder.build(),
            "When commandType is unknown, appropriate exception is thrown");
    }

    @Test
    void testBuildNoSufficientArguments() {
        assertThrows(BadCommandArgumentsException.class, () ->
                builder
                    .type(VaultCommandBuilder.CommandType.LOGIN)
                    .user(new User("Me"))
                    .build(),
            "Login needs user and password");
        assertThrows(BadCommandArgumentsException.class, () ->
                builder
                    .type(VaultCommandBuilder.CommandType.REGISTER)
                    .password(Password.of("Pass"))
                    .build(),
            "Register needs user, password and password duplicate");
        assertThrows(BadCommandArgumentsException.class, () ->
                builder
                    .type(VaultCommandBuilder.CommandType.RETRIEVE)
                    .build(),
            "Retrieve needs also a website");
    }

    @Test
    void testBuildReturnsRegisterCommand() throws BadCommandArgumentsException {
        VaultCommand command = builder.type(VaultCommandBuilder.CommandType.REGISTER)
            .user(new User("Me"))
            .password(Password.of("abcd"))
            .passwordDuplicate(Password.of("abcd"))
            .build();
        assertEquals(RegisterVaultCommand.class, command.getClass(),
            "Builder does not build command of appropriate class");
    }

    @Test
    void testBuildRegisterCommandFails() {
        builder.type(VaultCommandBuilder.CommandType.REGISTER)
            .user(new User("Me"))
            .password(Password.of("abcd"))
            .passwordDuplicate(Password.of("acd"));
        assertThrows(BadCommandArgumentsException.class, () -> builder.build(),
            "Password and password duplicate don't match");
    }

    @Test
    void testBuildReturnsAddCommand() throws BadCommandArgumentsException {
        VaultCommand command = builder.type(VaultCommandBuilder.CommandType.ADD)
            .user(new User("Me"))
            .website(new Website("google.com"))
            .owner(new User("Me"))
            .password(Password.of("oinvdiosnd"))
            .build();
        assertEquals(AddPasswordVaultCommand.class, command.getClass(),
            "Builder does not build command of appropriate class");
    }

    @Test
    void testBuildReturnsRetrieveCommand() throws BadCommandArgumentsException {
        VaultCommand command = builder.type(VaultCommandBuilder.CommandType.RETRIEVE)
            .owner(new User("Me"))
            .password(Password.of("dfsddsfs"))
            .website(new Website("dfsd"))
            .build();
        assertEquals(RetrieveVaultCommand.class, command.getClass(),
            "Builder does not build command of appropriate class");
    }

    @Test
    void testBuildReturnsLogoutCommand() throws BadCommandArgumentsException {
        VaultCommand command = builder.type(VaultCommandBuilder.CommandType.LOGOUT)
            .owner(new User("Me"))
            .website(new Website("dfsd"))
            .build();
        assertEquals(LogoutVaultCommand.class, command.getClass(),
            "Builder does not build command of appropriate class");
    }

    @Test
    void testBuildReturnsGenerateCommand() throws BadCommandArgumentsException {
        VaultCommand command = builder.type(VaultCommandBuilder.CommandType.ADD)
            .user(new User("Me"))
            .website(new Website("google.com"))
            .owner(new User("Me"))
            .password(null)
            .passwordLength(8354)
            .build();
        assertEquals(GeneratePaswordVaultCommand.class, command.getClass(),
            "Builder does not build command of appropriate class");
    }
}
