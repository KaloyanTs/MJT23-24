package bg.sofia.uni.fmi.mjt.passvault.vault;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.PasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.password.PasswordGenerator;
import bg.sofia.uni.fmi.mjt.passvault.server.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Vault {

    private static final int GEN_PASS_LENGTH = 10;
    private final Map<User, Map<Website, Password>> data;
    private final PasswordChecker checker;
    private final Set<User> activeUsers;
    private final Map<User, ScheduledFuture<?>> activity;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    public Vault() {
        //todo remove from here; badly looking
        checker = new PasswordChecker();
        activeUsers = new HashSet<>();
        data = new ConcurrentHashMap<>();
        activity = new HashMap<>();
    }


    private void updateActivity(User user) {
        if (!activeUsers.contains(user)) {
            throw new IllegalStateException("Given user is not logged in");
        }

        activity.get(user).cancel(false);
        activity.put(user, executorService.schedule(() -> {
            activeUsers.remove(user);
            System.out.println(user.name() + " forced logout...");
        }, 1, TimeUnit.MINUTES));
    }

    public Response addPassword(User user, Website website, Password password) {
        //todo write in file
        data.computeIfAbsent(user, k -> new HashMap<>());
        data.get(user).put(website, password);
        //todo what to return as a response (added pasword and operation info)
    }

    public Response removePassword(User user, Website website) {
        //todo remove from file
        if (data.get(user) == null || data.get(user).get(website) == null) {
            return new Response("Bad arguments: no such user or password assigned to it and give website!");
        }
        data.get(user).remove(website);
        return new Response("Password successfully removed");
    }

    public Response registerUser(User user, Password password) {
        try (FileWriter fileWriter = new FileWriter("users.pass", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             FileOutputStream fos = new FileOutputStream(user.name() + ".pass", false)) {

            bufferedWriter.write(user.name() + " " + password.getCiphered("SHA256"));
            bufferedWriter.newLine();

        } catch (IOException e) {
            throw new UnsupportedOperationException("todo");
            //todo consider whether something can be done (bad response maybe)
        }
        return new Response("User registered successfully!");
    }

    public Response logout(User user) {
        //todo consider if anything other must be done
        if (activeUsers.remove(user)) {
            return new Response("User logged out successfully!");
        } else {
            return new Response("User has already been logged out!");
        }
    }

    public Response login(User user, Password password) {
        //todo replace with check if password matches !!!!!!!!!!!!!
        activeUsers.add(user);
        updateActivity(user);

        return new Response("User logged in successfully!");
    }

    public Response retrieveCredentials(Website website, User user) {

    }
}
