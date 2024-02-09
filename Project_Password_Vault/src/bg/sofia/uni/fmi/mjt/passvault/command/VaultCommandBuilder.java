package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.exception.BadCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public class VaultCommandBuilder {

    public enum CommandType {
        REGISTER,
        LOGIN,
        LOGOUT,
        ADD,
        REMOVE
    }

    private final Vault vault;
    private CommandType type;
    private User user;
    private Password password;
    private Website website;
    private Integer passwordLength;

    public VaultCommandBuilder(Vault vault) {
        this.vault = vault;
        this.passwordLength = null;
        this.type = null;
        this.user = null;
        this.password = null;
        this.website = null;
    }

    public VaultCommandBuilder type(CommandType commandType) {
        this.type = commandType;
        return this;
    }

    public VaultCommandBuilder user(User user) {
        this.user = user;
        return this;
    }

    public VaultCommandBuilder password(Password password) {
        this.password = password;
        return this;
    }

    public VaultCommandBuilder website(Website website) {
        this.website = website;
        return this;
    }

    public VaultCommandBuilder passwordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        return this;
    }

    private void assertProvided(Object... args) throws BadCommandArgumentsException {
        for (Object o : args) {
            if (o == null)
                throw new BadCommandArgumentsException("Command could not be created due to lack of necessary " +
                    "arguments");
        }
    }

    public VaultCommand build() throws BadCommandArgumentsException {
        switch (type) {
            case REGISTER -> {
                assertProvided(user, password);
                return new RegisterVaultCommand(vault, user, password);
            }
            case LOGIN -> {
                assertProvided(user);
                return new LoginVaultCommand(vault, user, password);
            }
            case LOGOUT -> {
                assertProvided(user);
                return new LogoutVaultCommand(vault, user);
            }
            case ADD -> {
                assertProvided(user, website);
                if (password == null) {
                    assertProvided(passwordLength);
                    return new GeneratePaswordVaultCommand(vault, user, website, passwordLength);
                } else {
                    return new AddPasswordVaultCommand(vault, user, website, password);
                }
            }
            case REMOVE -> {
                assertProvided(user, website);
                return new RemovePasswordVaultCommand(vault, user, website);
            }
        }
        throw new UnsupportedOperationException("Unknown command type...");
    }
}
