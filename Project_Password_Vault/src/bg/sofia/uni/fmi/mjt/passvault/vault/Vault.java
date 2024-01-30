package bg.sofia.uni.fmi.mjt.passvault.vault;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.PasswordGenerator;
import bg.sofia.uni.fmi.mjt.passvault.server.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.util.Map;

public class Vault {

    Map<User, Map<Website, Password>> data;

    private static class VaultHelper {
        private static final Vault INSTANCE = new Vault();
    }

    public static Vault getInstance() {
        return VaultHelper.INSTANCE;
    }

    public Response addPassword(User user, Website website, Password password) {
        //todo implement
        return null;
    }
}
