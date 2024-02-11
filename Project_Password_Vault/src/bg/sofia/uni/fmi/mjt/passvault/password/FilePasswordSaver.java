package bg.sofia.uni.fmi.mjt.passvault.password;

import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FilePasswordSaver implements PasswordSaver {
    @Override
    public void newOwner(User owner) {
        //todo create owner's personal file with passwords
    }

    @Override
    public void savePassword(User owner, Website website, User username, Password password) {
        //todo implement
    }

    @Override
    public void removePassword(User owner, Website website) {
        //todo implement
    }

    @Override
    public void addUserToAll(User user, Password password) {
        try (FileWriter fileWriter = new FileWriter("users.pass", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            bufferedWriter.write(user.name() + " " + password.getCiphered("SHA256"));
            bufferedWriter.newLine();

        } catch (IOException e) {
            throw new UnsupportedOperationException("Unexpected problem while writing to a file", e);
        }
    }
}
