public class BrokenKeyboard {

    private static boolean canBeTyped(String word, String brokenKeys) {
        for (char c : word.toCharArray())
            if (brokenKeys.indexOf(String.valueOf(c)) != -1) return false;
        return true;
    }

    public static int calculateFullyTypedWords(String message, String brokenKeys) {
        String[] words = message.toLowerCase().split(" ");
        int res = 0;
        for (String s : words) if (!s.isBlank() && canBeTyped(s, brokenKeys)) ++res;
        return res;
    }
}
