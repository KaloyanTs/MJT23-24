package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public class RemovePasswordVaultCommand implements VaultCommand {

    private final Vault vault;
    private final User user;
    private final Website website;

    public RemovePasswordVaultCommand(Vault vault, User user, Website website) {
        if (vault == null || user == null || website == null) {
            throw new IllegalArgumentException("Null cannot be an argument...");
        }
        this.vault = vault;
        this.user = user;
        this.website = website;
    }

    @Override
    public Response execute() {
        return vault.removePassword(user, website);
    }
}
