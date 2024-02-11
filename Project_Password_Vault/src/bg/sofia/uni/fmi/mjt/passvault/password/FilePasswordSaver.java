package bg.sofia.uni.fmi.mjt.passvault.password;

import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

public class FilePasswordSaver implements PasswordSaver {
    @Override
    public void newOwner(User owner) {
        //todo implement
    }

    @Override
    public void savePassword(User owner, Website website, User username, Password password) {
        //todo implement
    }

    @Override
    public void removePassword(User owner, Website website) {
        //todo implement
    }
}
