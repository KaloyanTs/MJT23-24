package bg.sofia.uni.fmi.mjt.passvault.vault;

import bg.sofia.uni.fmi.mjt.passvault.exception.NoPasswordRegisteredException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.saver.PasswordSaver;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.utility.KeyValuePair;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.util.HashMap;
import java.util.Map;

public class UserContainer {

    private final User owner;
    private final Map<Website, KeyValuePair<User, Password>> container;
    private final PasswordSaver saver;

    public UserContainer(User owner, PasswordSaver saver) {
        this.owner = owner;
        this.saver = saver;
        this.saver.newOwner(owner);
        container = new HashMap<>();
        saver.newOwner(owner);
    }

    public void addPassword(Website website, User user, Password password) {
        saver.savePassword(owner, website, user, password);
        container.put(website, new KeyValuePair<>(user, password));
    }

    public void removePassword(Website website) {
        saver.removePassword(owner, website);
        container.remove(website);
    }

    public KeyValuePair<User, Password> retrieve(Website website) throws NoPasswordRegisteredException {
        KeyValuePair<User, Password> res = container.get(website);
        if (res == null) {
            throw new NoPasswordRegisteredException("No credentials for the given website saved...");
        }
        return res;
    }

    public User getOwner() {
        return owner;
    }
}
