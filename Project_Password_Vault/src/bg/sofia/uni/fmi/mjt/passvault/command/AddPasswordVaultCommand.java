package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.server.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.util.Optional;

public class AddPasswordVaultCommand implements VaultCommand {
    private final Vault vault;
    private final User user;
    private final Website website;
    private final Password password;

    public AddPasswordVaultCommand(Vault vault, User user, Website website, Password password) {
        if (vault == null || user == null || website == null || password == null) {
            throw new IllegalArgumentException("Null cannot be given as argument...");
        }
        this.vault = vault;
        this.user = user;
        this.website = website;
        this.password = password;
    }

    @Override
    public Response execute() {
        if (vault.getPasswordChecker().checkPasswordIsCompromised(password)) {
            return new Response("Given password has been compromised!", Optional.of(password));
        }
        return vault.addPassword(user, website, password);
    }
}
