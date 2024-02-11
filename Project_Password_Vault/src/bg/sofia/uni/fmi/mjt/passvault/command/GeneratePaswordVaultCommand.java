package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotLoggedInException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.PasswordGenerator;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public class GeneratePaswordVaultCommand implements VaultCommand {
    private final Vault vault;
    private final User user;
    private final Website website;
    private final int length;
    private final User owner;

    public GeneratePaswordVaultCommand(Vault vault, User owner, Website website, User user, int length) {
        if (vault == null || user == null || website == null) {
            throw new IllegalArgumentException("Null cannot be given as an argument...");
        }
        this.vault = vault;
        this.user = user;
        this.website = website;
        this.length = length;
        this.owner = owner;
    }

    @Override
    public Response execute() {
        Password password = PasswordGenerator.getInstance().generatePassword(length);
        try {
            return vault.addPassword(owner, website, user, password);
        } catch (UserNotLoggedInException e) {
            return new Response(e.getMessage(), null, null);
        }
    }
}
