package bg.sofia.uni.fmi.mjt.passvault.password.saver;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public interface PasswordSaver {
    void newOwner(User owner);

    void savePassword(User owner, Website website, User username, Password password);

    void removePassword(User owner, Website website);

    void addUserToAll(User user, Password password);
}
