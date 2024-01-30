package bg.sofia.uni.fmi.mjt.passvault.vault;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.PasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.password.PasswordGenerator;
import bg.sofia.uni.fmi.mjt.passvault.server.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Vault {

    Map<User, Map<Website, Password>> data;
    PasswordChecker checker;

    public Vault() {
        //todo remove from here; badly looking
        checker = new PasswordChecker();
    }

    public Response addPassword(User user, Website website, Optional<Password> password) {

        if (password.isPresent() && !checker.checkPasswordIsCompromised(password.get())) {
            return new Response("The password is compromised! Cannot be added.");
        }
        if (password.isEmpty()) {
            password = Optional.of(PasswordGenerator.getInstance().generatePassword(10));
        }
        Response genResponse = new Response("Password generated: " + password.get().getDecrypted());
        data.computeIfAbsent(user, k -> new HashMap<>());
        data.get(user).put(website, password.get());
        return Response.combine(genResponse, new Response("The password was added successfully"));
    }
}
