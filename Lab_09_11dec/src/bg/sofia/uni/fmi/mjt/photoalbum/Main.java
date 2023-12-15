package bg.sofia.uni.fmi.mjt.photoalbum;

public class Main {
    public static void main(String[] args) {
        ParallelMonochromeAlbumCreator p = new ParallelMonochromeAlbumCreator(10);

        long d = System.nanoTime();
        p.processImages("D:\\JavaProjects\\Lab_09_11dec\\same", "D:\\JavaProjects\\Lab_09_11dec\\outPics");
        System.out.println("Took me:\t" + (double) (System.nanoTime() - d) / 1_000_000_000 + "s");
    }
}

