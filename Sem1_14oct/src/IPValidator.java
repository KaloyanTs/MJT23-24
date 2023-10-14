import java.util.Arrays;

public class IPValidator {

    private static boolean isNumber(String s) {
        if (s.isEmpty()) return false;
        if (s.equals("0")) return true;
        if (s.charAt(0) == '0') return false;
        int res = Integer.parseInt(s);
        return res > 0 && res <= 255;
    }

    public static boolean validateIPv4Address(String str) {
        String[] nums = str.split("\\.");
        if (nums.length != 4) return false;
        for (String n : nums) {
            if (!isNumber(n)) return false;
        }
        return true;
    }
}
