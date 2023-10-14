import java.util.Arrays;

public class JumpGame {

    private static int getSum(int[] arr, int from, int to) {
        return arr[from] - arr[to + 1];
    }

    public static boolean canWin(int[] array) {
        int[] winning = new int[array.length];
        int[] sums = new int[array.length + 1];
        winning[array.length - 1] = 1;
        sums[array.length - 1] = 1;
        for (int i = array.length - 2; i >= 0; --i) {
            winning[i] = (getSum(sums, i + 1, Math.min(array.length - 1, i + array[i])) > 0 ? 1 : 0);
            sums[i] = sums[i + 1] + winning[i];
        }
        return winning[0] == 1;
    }

}
