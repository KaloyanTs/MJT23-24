package bg.sofia.uni.fmi.mjt.passvault.password;

import java.util.Random;

public class PasswordGenerator {

    private final Random random;
    private static final int LOW_CHAR = 41;
    private static final int HIGH_CHAR = 126;

    private PasswordGenerator() {
        random = new Random();
    }

    private static class PasswordGeneratorHelper {
        private static final PasswordGenerator INSTANCE = new PasswordGenerator();
    }

    public static PasswordGenerator getInstance() {
        return PasswordGenerator.PasswordGeneratorHelper.INSTANCE;
    }

    public Password generatePassword(int length) {
        String generatedString =
            random.ints(LOW_CHAR, HIGH_CHAR + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return Password.of(generatedString);
    }
}
