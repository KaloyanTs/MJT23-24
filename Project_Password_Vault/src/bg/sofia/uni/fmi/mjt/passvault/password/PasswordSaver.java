package bg.sofia.uni.fmi.mjt.passvault.password;

import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public interface PasswordSaver {
    //todo inject into vault, which passes to UserContainer
    void newOwner(User owner);

    void savePassword(User owner, Website website, User username, Password password);

    void removePassword(User owner, Website website);

    void addUserToAll(User user, Password password);
}
