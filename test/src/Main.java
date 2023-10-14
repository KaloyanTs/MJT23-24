import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) {
        char ch = 'b';
        int z = switch (ch) {
            case 'a' -> 1;
            case 'b', 'c' -> 12;
            default -> 27;
        };
        var r = switch (ch) {
            case 'a':
                yield 1;
            case 'b':
                yield 4;
            default:
                yield 27;
        };
        ++r;
        System.out.println(z);
        System.out.println(r);
        int arr[] = new int[6];
        Arrays.fill(arr,10);

        System.out.println(Arrays.toString(arr));
    }
}