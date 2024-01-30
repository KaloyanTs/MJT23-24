package bg.sofia.uni.fmi.mjt.passvault.vault;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.PasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.server.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.util.HashMap;
import java.util.Map;

public class Vault {

    Map<User, Map<Website, Password>> data;
    PasswordChecker checker;

    private Vault() {
        //todo remove from here; badly looking
        checker = new PasswordChecker();
    }

    private static class VaultHelper {
        private static final Vault INSTANCE = new Vault();
    }

    public static Vault getInstance() {
        return VaultHelper.INSTANCE;
    }

    public Response addPassword(User user, Website website, Password password) {

        if (!checker.checkPassword(password)) {
            return new Response();
            //todo implement bad response for the client
        }
        data.computeIfAbsent(user, k -> new HashMap<>());
        data.get(user).put(website, password);
        return new Response();
        //todo implement good response
    }
}
