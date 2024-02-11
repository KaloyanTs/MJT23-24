package bg.sofia.uni.fmi.mjt.passvault.vault;

import bg.sofia.uni.fmi.mjt.passvault.exception.NoPasswordRegisteredException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.utility.KeyValuePair;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.util.HashMap;
import java.util.Map;

public class UserContainer {

    private User owner;
    private Map<Website, KeyValuePair<User, Password>> container;

    public UserContainer(User owner) {
        //todo create a file in which passwords will be kept
        this.owner = owner;
        container = new HashMap<>();
    }

    public void addPassword(Website website, User user, Password password) {
        //todo append password to file
        container.put(website, new KeyValuePair<>(user, password));
    }

    public void removePassword(Website website) {
        //todo remove from file
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
