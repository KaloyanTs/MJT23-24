package bg.sofia.uni.fmi.mjt.passvault.command;

import bg.sofia.uni.fmi.mjt.passvault.exception.NoPasswordRegisteredException;
import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotLoggedInException;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public class RetrieveVaultCommand implements VaultCommand {
    private final Vault vault;
    private final User owner;
    private final Website website;

    public RetrieveVaultCommand(Vault vault, User owner, Website website) {
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
            Response res = vault.retrieveCredentials(owner, website);
            System.out.println(res.password().getDecrypted());
            return res;
        } catch (NoPasswordRegisteredException e) {
            return new Response(
                "\"" + owner.name() + "\" has no password stored for website \"" + website.url() + "\"" +
                    ".", null, null);
        } catch (UserNotLoggedInException e) {
            return new Response(e.getMessage(), null, null);
        }
    }
}
