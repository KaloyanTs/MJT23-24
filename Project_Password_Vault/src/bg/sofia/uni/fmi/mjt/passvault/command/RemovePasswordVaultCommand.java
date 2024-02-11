package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotLoggedInException;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public class RemovePasswordVaultCommand implements VaultCommand {

    private final Vault vault;
    private final User owner;
    private final Website website;

    public RemovePasswordVaultCommand(Vault vault, User owner, Website website) {
        if (vault == null || owner == null || website == null) {
            throw new IllegalArgumentException("Null cannot be an argument...");
        }
        this.vault = vault;
        this.owner = owner;
        this.website = website;
    }

    @Override
    public Response execute() {
        try {
            return vault.removePassword(owner, website);
        } catch (UserNotLoggedInException e) {
            return new Response(e.getMessage(), null, null);
        }
    }
}
