package bg.sofia.uni.fmi.mjt.passvault.vault;

import bg.sofia.uni.fmi.mjt.passvault.exception.NoPasswordRegisteredException;
import bg.sofia.uni.fmi.mjt.passvault.exception.UserNotRegisteredException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.password.PasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.server.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Vault {
    private final Map<User, Map<Website, Password>> data;
    private final Set<User> activeUsers;
    private final Map<User, ScheduledFuture<?>> activity;
    private final ScheduledExecutorService executorService;
    private final PasswordChecker passwordChecker;

    public Vault(PasswordChecker passwordChecker) {
        this.passwordChecker = passwordChecker;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.activeUsers = new HashSet<>();
        this.data = new ConcurrentHashMap<>();
        this.activity = new HashMap<>();
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
        data.computeIfAbsent(user, x -> new HashMap<>());
        data.get(user).put(website, password);
        return new Response("Successfully added", Optional.of(password));
        //todo what to return as a response (added password and operation info)
    }

    public Response removePassword(User user, Website website) {
        //todo remove from file
        if (data.get(user) == null || data.get(user).get(website) == null) {
            //todo throwing exception instead and catching it in the command????????????????????
            return new Response("Bad arguments: no such user or password assigned to it and given website!",
                Optional.empty());
        }
        Password removedPassword = data.get(user).remove(website);
        return new Response("Password successfully removed", Optional.of(removedPassword));
    }

    public Response registerUser(User user, Password password) {
        try (FileWriter fileWriter = new FileWriter("users.pass", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            bufferedWriter.write(user.name() + " " + password.getCiphered("SHA256"));
            bufferedWriter.newLine();

        } catch (IOException e) {
            throw new UnsupportedOperationException("Unexpected problem while writing to a file", e);
        }
        return new Response("User registered successfully!", Optional.empty());
    }

    public Response logout(User user) {
        //todo consider if anything other must be done
        if (activeUsers.remove(user)) {
            return new Response("User logged out successfully!", Optional.empty());
        } else {
            return new Response("User has already been logged out!", Optional.empty());
        }
    }

    public Response login(User user, Password password) {
        //todo replace with check if password matches !!!!!!!!!!!!!
        activeUsers.add(user);
        updateActivity(user);

        return new Response("User logged in successfully!", Optional.empty());
    }

    public Response retrieveCredentials(Website website, User user)
        throws NoPasswordRegisteredException, UserNotRegisteredException {
        if (website == null || user == null) {
            throw new IllegalArgumentException("Null cannot be an argument...");
        }
        if (data.get(user) == null) {
            throw new UserNotRegisteredException("Given user is not registered in the vault...");
        }
        Password password = data.get(user).get(website);
        if (password == null) {
            //todo catch and convert to response in command execute code
            throw new NoPasswordRegisteredException("Given user and website don't match any password...");
        }
        return new Response("Password retrieved successfully", Optional.of(password));
    }

    public PasswordChecker getPasswordChecker() {
        return passwordChecker;
    }
}
