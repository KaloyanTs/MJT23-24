public class Main {

    public static void main(String[] args) {
        System.out.println(IPValidator.validateIPv4Address("192.168.1.1") == true);
        System.out.println(IPValidator.validateIPv4Address("192.168.1.0") == true);
        System.out.println(IPValidator.validateIPv4Address("192.168.1.00") == false);
        System.out.println(IPValidator.validateIPv4Address("192.168@1.1") == false);
        System.out.println(IPValidator.validateIPv4Address("168 ") == false);
        System.out.println(JumpGame.canWin(new int[]{2, 3, 1, 1, 0}) == true);
        System.out.println(JumpGame.canWin(new int[]{3, 2, 1, 0, 0}) == false);
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("i love mjt", "qsf3o") == 2);
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("secret      message info      ", "sms") == 1);
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("dve po 2 4isto novi beli kecove", "o2sf") == 2);
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("     ", "asd") == 0);
        System.out.println(BrokenKeyboard.calculateFullyTypedWords(" - 1 @ - 4", "s") == 5);
    }
}
