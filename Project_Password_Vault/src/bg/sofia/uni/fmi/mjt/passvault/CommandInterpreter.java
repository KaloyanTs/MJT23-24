package bg.sofia.uni.fmi.mjt.passvault;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.server.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.util.Optional;

public class CommandInterpreter {

    private static final int ADD_ARGUMENTS = 4;
    private static final int ADD_USER_ARGUMENT = 2;
    private static final int ADD_WEBSITE_ARGUMENT = 1;
    private static final int ADD_PASSWORD_ARGUMENT = 3;
    private static final int GENERATE_ARGUMENTS = 3;
    private static final int GENERATE_USER_ARGUMENT = 2;
    private static final int GENERATE_WEBSITE_ARGUMENT = 1;
    private final Vault vault;

    //todo find usage
    private User currentUser;

    public CommandInterpreter(Vault vault) {
        this.vault = vault;
    }

    public Response intepretate(String command) {
        String[] parts = command.trim().split("\\s+");
        return switch (parts[0]) {
            case "add-password" -> {
                if (parts.length < ADD_ARGUMENTS)
                    yield new Response("Usage: add-password <website> <user> <password>");
                yield vault.addPassword(
                    User.of(parts[ADD_USER_ARGUMENT]),
                    Website.of(parts[ADD_WEBSITE_ARGUMENT]),
                    Optional.of(Password.of(parts[ADD_PASSWORD_ARGUMENT]))
                );
            }
            case "generate-password" -> {
                if (parts.length < GENERATE_ARGUMENTS)
                    yield new Response("Usage: generate-password <website> <user>");
                yield vault.addPassword(
                    User.of(parts[GENERATE_USER_ARGUMENT]),
                    Website.of(parts[GENERATE_WEBSITE_ARGUMENT]),
                    Optional.empty());
                //todo implement more
            }
            default -> new Response("Unknown command...");
        };
    }
}
