package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.server.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;

public class LogoutVaultCommand implements VaultCommand {

    private final Vault vault;
    private final User user;

    public LogoutVaultCommand(Vault vault, User user) {
        if (vault == null || user == null) {
            throw new IllegalArgumentException("Null cannot be given as argument...");
        }
        this.vault = vault;
        this.user = user;
    }

    @Override
    public Response execute() {
        return vault.logout(user);
    }
}
