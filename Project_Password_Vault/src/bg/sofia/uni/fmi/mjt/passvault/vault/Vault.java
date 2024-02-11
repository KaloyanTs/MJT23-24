package bg.sofia.uni.fmi.mjt.passvault.vault;

import bg.sofia.uni.fmi.mjt.passvault.exception.NoPasswordRegisteredException;
import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotLoggedInException;
import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotRegisteredException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.checker.PasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.utility.KeyValuePair;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Vault {
    private static final int SECONDS_TO_LOGOUT = 20;
    private final Map<User, UserContainer> data;
    private final ConcurrentSkipListSet<User> activeUsers;
    private final Map<User, ScheduledFuture<?>> activity;
    private final ScheduledExecutorService executorService;
    private final PasswordChecker passwordChecker;
    private final Map<User, Password> userPassword;

    public Vault(PasswordChecker passwordChecker) {
        this.passwordChecker = passwordChecker;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.data = new ConcurrentHashMap<>();
        this.activity = new HashMap<>();
        this.userPassword = new HashMap<>();
        this.activeUsers = new ConcurrentSkipListSet<>();
        try (FileWriter fileWriter = new FileWriter("users.pass", false)) {
        } catch (IOException e) {
            throw new IllegalStateException("Error on file creation...", e);
        }
    }

    public void assertLoggedIn(User user) throws UserNotLoggedInException {
        if (!activeUsers.contains(user)) {
            throw new UserNotLoggedInException("Not logged in!");
        }
    }

    private void updateActivity(User user) {
        if (activity.get(user) != null)
            activity.get(user).cancel(false);
        activity.put(user, executorService.schedule(() -> {
            activeUsers.remove(user);
            System.out.println(user.name() + " forced logout...");
        }, SECONDS_TO_LOGOUT, TimeUnit.SECONDS));
    }

    public Response addPassword(User owner, Website website, User username, Password password)
        throws UserNotLoggedInException {
        assertLoggedIn(owner);
        data.get(owner).addPassword(website, username, password);
        return new Response("Successfully added", username, password);
    }

    public Response removePassword(User owner, Website website) throws UserNotLoggedInException {
        assertLoggedIn(owner);
        data.get(owner).removePassword(website);
        return new Response("Password successfully removed", null, null);
    }

    public Response registerUser(User user, Password password) {
        try (FileWriter fileWriter = new FileWriter("users.pass", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            bufferedWriter.write(user.name() + " " + password.getCiphered("SHA256"));
            bufferedWriter.newLine();

        } catch (IOException e) {
            throw new UnsupportedOperationException("Unexpected problem while writing to a file", e);
        }
        userPassword.put(user, password);
        data.put(user, new UserContainer(user));
        return new Response("User registered successfully!", null, null);
    }

    public Response logout(User user) throws UserNotLoggedInException {
        assertLoggedIn(user);
        activeUsers.remove(user);
        return new Response("User has already been logged out!", null, null);
    }

    public Response login(User user, Password password) {
        //todo consider if correct
        if (userPassword.get(user) == null) {
            return new Response("User is not registered!", null, null);
        }
        if (!Password.areEqual(userPassword.get(user), password)) {
            return new Response("Wrong password!", null, null);
        }
        activeUsers.add(user);
        updateActivity(user);
        return new Response("User logged in successfully!", user, null);
    }

    public Response retrieveCredentials(User owner, Website website)
        throws NoPasswordRegisteredException, UserNotRegisteredException, UserNotLoggedInException {
        assertLoggedIn(owner);
        KeyValuePair<User, Password> res = data.get(owner).retrieve(website);
        return new Response("Password retrieved successfully", res.key(), res.value());
    }

    public PasswordChecker getPasswordChecker() {
        return passwordChecker;
    }
}
