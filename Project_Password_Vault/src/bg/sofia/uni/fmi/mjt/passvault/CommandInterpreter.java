package bg.sofia.uni.fmi.mjt.passvault;

import bg.sofia.uni.fmi.mjt.passvault.command.VaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.VaultCommandBuilder;
import bg.sofia.uni.fmi.mjt.passvault.exception.BadCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.server.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.utility.KeyValuePair;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class CommandInterpreter {

    private static final int THREE = 3;
    private static final Map<String, List<KeyValuePair<String, Integer>>> USAGES;
    private static final Map<String, VaultCommandBuilder.CommandType> WORD_TO_COMMAND_TYPE;
    private final Vault vault;
    private final VaultCommandBuilder commandBuilder;
    private final Map<String, BiConsumer<String[], Integer>> wordsToActions;


    static {
        USAGES = new HashMap<>();
        USAGES.put("register", List.of(new KeyValuePair<>("user", 1), new KeyValuePair<>("password", 2),
            new KeyValuePair<>("password-duplicate", THREE)));
        USAGES.put("login", List.of(new KeyValuePair<>("user", 1), new KeyValuePair<>("password", 2)));
        USAGES.put("logout", List.of(new KeyValuePair<>("user", 1)));
        USAGES.put("retrieve-credentials", List.of(new KeyValuePair<>("website", 1), new KeyValuePair<>("user", 2)));
        USAGES.put("generate-password", List.of(new KeyValuePair<>("website", 1), new KeyValuePair<>("user", 2)));
        USAGES.put("add-password", List.of(new KeyValuePair<>("website", 1), new KeyValuePair<>("user", 2),
            new KeyValuePair<>("password", THREE)));
        USAGES.put("remove-password", List.of(new KeyValuePair<>("website", 1), new KeyValuePair<>("user", 2)));

        WORD_TO_COMMAND_TYPE = new HashMap<>();
        WORD_TO_COMMAND_TYPE.put("add-password", VaultCommandBuilder.CommandType.ADD);
        WORD_TO_COMMAND_TYPE.put("retrieve-password", VaultCommandBuilder.CommandType.RETRIEVE);
        WORD_TO_COMMAND_TYPE.put("remove-password", VaultCommandBuilder.CommandType.REMOVE);
        WORD_TO_COMMAND_TYPE.put("login", VaultCommandBuilder.CommandType.LOGIN);
        WORD_TO_COMMAND_TYPE.put("logout", VaultCommandBuilder.CommandType.LOGOUT);
        //todo add more
    }

    public CommandInterpreter(Vault vault) {
        this.vault = vault;
        this.commandBuilder = new VaultCommandBuilder(this.vault);
        wordsToActions = new HashMap<>();
        wordsToActions.put("user", (args, index) -> commandBuilder.user(new User(args[index])));
        wordsToActions.put("password", (args, index) -> commandBuilder.password(Password.of(args[index])));
        wordsToActions.put("website", (args, index) -> commandBuilder.user(new User(args[index])));
        wordsToActions.put("password-duplicate",
            (args, index) -> commandBuilder.passwordDuplicate(Password.of(args[index])));
    }

    public Response intepretate(String commandLine) {
        String[] parts = commandLine.trim().split("\\s+");
        if (parts.length < 1) {
            return new Response("", Optional.empty());
        }
        if (parts[0].equals("disconnect")) {
            return null;
            //todo in server getting null from this method would mean end connection
        }

        WORD_TO_COMMAND_TYPE.get(parts[0]);
        for (KeyValuePair<String, Integer> pair : USAGES.get(parts[0])) {
            wordsToActions.get(pair.key()).accept(parts, pair.value());
        }
        VaultCommand command;
        try {
            command = commandBuilder.build();
        } catch (BadCommandArgumentsException e) {
            return new Response("Bad usage... (see manual)", Optional.empty());
        }

        return command.execute();
    }
}
