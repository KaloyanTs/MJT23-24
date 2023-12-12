package bg.sofia.uni.fmi.mjt.photoalbum;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {

    private final int imageProcessorsCount;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        Path source = Path.of(sourceDirectory);
        Path output = Path.of(outputDirectory);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source, "*.jpg")) {
            for (Path file : stream) {
// todo implement producer consumer logic here
                Image.saveImage(Image.loadImage(file), STR."\{outputDirectory}\\\{file.getFileName()}");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while processing directory...", e);
        }
    }
}
