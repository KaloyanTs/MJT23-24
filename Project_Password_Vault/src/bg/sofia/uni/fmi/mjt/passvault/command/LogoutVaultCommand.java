package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotLoggedInException;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;

public class LogoutVaultCommand implements VaultCommand {

    private final Vault vault;
    private final User owner;

    public LogoutVaultCommand(Vault vault, User owner) {
        if (vault == null || owner == null) {
            throw new IllegalArgumentException("Null cannot be given as argument...");
        }
        this.vault = vault;
        this.owner = owner;
    }

    @Override
    public Response execute() {
        try {
            return vault.logout(owner);
        } catch (UserNotLoggedInException e) {
            return new Response(e.getMessage(), null, null);
        }
    }
}
