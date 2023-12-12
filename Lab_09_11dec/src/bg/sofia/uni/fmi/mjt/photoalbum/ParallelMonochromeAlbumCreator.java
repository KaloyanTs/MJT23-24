package bg.sofia.uni.fmi.mjt.photoalbum;

import bg.sofia.uni.fmi.mjt.photoalbum.threads.ConsumerThread;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {

    private final int imageProcessorsCount;
    List<ConsumerThread> consumerThreads;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
        consumerThreads = new ArrayList<>(imageProcessorsCount);
        for (int i = 0; i < imageProcessorsCount; ++i) {

        }
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        Path source = Path.of(sourceDirectory);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source, "*.jpg")) {
            Iterator<Path> i = stream.iterator();
            ConsumerThread t;
            for (int j = 0; j < imageProcessorsCount; ++j) {
                t = new ConsumerThread(i, outputDirectory);
                consumerThreads.add(t);
                t.start();
            }
            for (ConsumerThread thread : consumerThreads) {
                thread.join();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while processing directory...", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread interrupted...", e);
        }
    }
}
