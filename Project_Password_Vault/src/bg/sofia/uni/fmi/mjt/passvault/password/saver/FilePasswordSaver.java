package bg.sofia.uni.fmi.mjt.passvault.password.saver;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FilePasswordSaver implements PasswordSaver {
    @Override
    public void newOwner(User owner) {
        String filePath = owner.name() + ".user";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected problem on saving to file...", e);
        }
    }

    @Override
    public void savePassword(User owner, Website website, User username, Password password) {
        removePassword(owner, website);
        String filePath = owner.name() + ".user";
        String newEntry = website.url() + "\t" + username.name() + "\t" + password.getEncrypted();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(newEntry);
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected problem on saving to file...", e);
        }
    }

    @Override
    public void removePassword(User owner, Website website) {
        String filePath = owner.name() + ".user";
        try (
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            List<String> lines = Files.readAllLines(Path.of(filePath));
            new FileWriter(filePath, false).close();
            for (String line : lines) {
                if (!line.contains(website.url())) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected problem on saving to file...", e);
        }
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
