package bg.sofia.uni.fmi.mjt.passvault.command.interpreter;

import bg.sofia.uni.fmi.mjt.passvault.client.Request;
import bg.sofia.uni.fmi.mjt.passvault.command.VaultCommand;
import bg.sofia.uni.fmi.mjt.passvault.command.VaultCommandBuilder;
import bg.sofia.uni.fmi.mjt.passvault.exception.BadCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.utility.KeyValuePair;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;
import bg.sofia.uni.fmi.mjt.passvault.website.Website;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        USAGES.put("retrieve-credentials", List.of(new KeyValuePair<>("website", 1)));
        USAGES.put("generate-password", List.of(new KeyValuePair<>("website", 1), new KeyValuePair<>("user", 2)));
        USAGES.put("add-password", List.of(new KeyValuePair<>("website", 1), new KeyValuePair<>("user", 2),
            new KeyValuePair<>("password", THREE)));
        USAGES.put("remove-password", List.of(new KeyValuePair<>("website", 1)));

        WORD_TO_COMMAND_TYPE = new HashMap<>();
        WORD_TO_COMMAND_TYPE.put("add-password", VaultCommandBuilder.CommandType.ADD);
        WORD_TO_COMMAND_TYPE.put("generate-password", VaultCommandBuilder.CommandType.ADD);
        WORD_TO_COMMAND_TYPE.put("retrieve-credentials", VaultCommandBuilder.CommandType.RETRIEVE);
        WORD_TO_COMMAND_TYPE.put("remove-password", VaultCommandBuilder.CommandType.REMOVE);
        WORD_TO_COMMAND_TYPE.put("login", VaultCommandBuilder.CommandType.LOGIN);
        WORD_TO_COMMAND_TYPE.put("logout", VaultCommandBuilder.CommandType.LOGOUT);
        WORD_TO_COMMAND_TYPE.put("register", VaultCommandBuilder.CommandType.REGISTER);
        //todo add more
    }

    public CommandInterpreter(Vault vault) {
        this.vault = vault;
        this.commandBuilder = new VaultCommandBuilder(this.vault);
        wordsToActions = new HashMap<>();
        wordsToActions.put("user", (args, index) -> commandBuilder.user(new User(args[index])));
        wordsToActions.put("password", (args, index) -> commandBuilder.password(Password.of(args[index])));
        wordsToActions.put("website", (args, index) -> commandBuilder.website(new Website(args[index])));
        wordsToActions.put("password-duplicate",
            (args, index) -> commandBuilder.passwordDuplicate(Password.of(args[index])));
    }

    public Response intepretate(Request request) {
        String[] parts = request.line().trim().split("\\s+");
        if (parts.length < 1 || parts[0].isEmpty()) {
            return new Response("", null, null);
        }
        if (parts[0].equals("disconnect")) {
            return null;
        }

        commandBuilder.owner(request.cookie().user());
        if (WORD_TO_COMMAND_TYPE.get(parts[0]) == null) {
            return new Response("Unknown command!", null, null);
        }
        commandBuilder.type(WORD_TO_COMMAND_TYPE.get(parts[0]));
        for (KeyValuePair<String, Integer> pair : USAGES.get(parts[0])) {
            try {
                wordsToActions.get(pair.key()).accept(parts, pair.value());
            } catch (ArrayIndexOutOfBoundsException e) {
                return new Response("Bad number of arguments...", null, null);
            }
        }
        VaultCommand command;
        try {
            command = commandBuilder.build();
        } catch (BadCommandArgumentsException e) {
            return new Response(e.getMessage(), null, null);
        }

        return command.execute();
    }
}
