package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public class VaultCommandBuilder {

    public enum CommandType {
        REGISTER,
        LOGIN,
        LOGOUT,
        ADD,
        REMOVE
    }

    private CommandType type;
    private User user;
    private Password password;
    private Website website;

    public VaultCommandBuilder() {
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

    private void assertProvided(Object... args) throws BadCommandArgumentsException {
        for (Object o : args) {
            if (o == null)
                throw new BadCommandArgumentsException("Command could not be created due to lack of necessary " +
                    "arguments");
        }
    }

    public VaultCommand build() throws BadCommandArgumentsException {
        return switch (type) {
            case REGISTER -> {
                assertProvided(user, password);
            }
            case LOGIN -> {
                assertProvided(user);
                //todo construct LoginCommand
            }
            case LOGOUT -> {
                assertProvided(user);
            }
            case ADD -> {
                assertProvided(user, website);
                //todo check whether add or generate
            }
            case REMOVE -> {
                assertProvided(user, website);
            }
        }
    }
}
