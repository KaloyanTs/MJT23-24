public class JumpGame {
    public static boolean canWin(int[] array) {
        for (int i = 0; i < array.length - 1; ++i)
            if (i + array[i] >= array.length - 1) return true;
        return false;
    }
}
