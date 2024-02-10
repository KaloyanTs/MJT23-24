package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;

public class LoginVaultCommand implements VaultCommand {

    private final Vault vault;
    private final User user;
    private final Password password;

    public LoginVaultCommand(Vault vault, User user, Password password) {
        if (vault == null || user == null || password == null) {
            throw new IllegalArgumentException("Null cannot be given as argument...");
        }
        this.vault = vault;
        this.user = user;
        this.password = password;
    }

    @Override
    public Response execute() {
        return vault.login(user, password);
    }
}
