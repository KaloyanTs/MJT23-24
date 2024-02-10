package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.exception.NoPasswordRegisteredException;
import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotRegisteredException;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public class RetrieveVaultCommand implements VaultCommand {
    private final Vault vault;
    private final User user;
    private final Website website;

    public RetrieveVaultCommand(Vault vault, User user, Website website) {
        if (vault == null || user == null || website == null) {
            throw new IllegalArgumentException("Null cannot be an argument...");
        }
        this.vault = vault;
        this.user = user;
        this.website = website;
    }

    @Override
    public Response execute() {
        try {
            return vault.retrieveCredentials(website, user);
        } catch (NoPasswordRegisteredException e) {
            return new Response("\"" + user.name() + "\" is not registered in the vault.", null);
        } catch (UserNotRegisteredException e) {
            return new Response("\"" + user.name() + "\" has no password stored for website \"" + website.url() + "\"" +
                ".", null);
        }
    }
}
