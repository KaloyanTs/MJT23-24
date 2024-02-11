package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotLoggedInException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public class AddPasswordVaultCommand implements VaultCommand {
    private final Vault vault;
    private final User user;
    private final Website website;
    private final Password password;
    private final User owner;

    public AddPasswordVaultCommand(Vault vault, User owner, Website website, User user, Password password) {
        if (owner == null || vault == null || user == null || website == null || password == null) {
            throw new IllegalArgumentException("Null cannot be given as argument...");
        }
        this.vault = vault;
        this.user = user;
        this.website = website;
        this.password = password;
        this.owner = owner;
    }

    @Override
    public Response execute() {
        try {
            vault.assertLoggedIn(owner);
            if (vault.getPasswordChecker().checkPasswordIsCompromised(password)) {
                return new Response("Given password has been compromised!", null, password);
            }
            return vault.addPassword(owner, website, user, password);
        } catch (UserNotLoggedInException e) {
            return new Response(e.getMessage(), null, null);
        }
    }
}
