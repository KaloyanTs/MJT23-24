package bg.sofia.uni.fmi.mjt.photoalbum;

import bg.sofia.uni.fmi.mjt.photoalbum.threads.ConsumerThread;
import bg.sofia.uni.fmi.mjt.photoalbum.threads.ProducerThread;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {

    private final int imageProcessorsCount;
    List<ConsumerThread> consumerThreads;
    List<ProducerThread> producerThreads;
    private final Queue<Image> queue;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
        consumerThreads = new ArrayList<>(imageProcessorsCount);
        producerThreads = new ArrayList<>(imageProcessorsCount);
        queue = new LinkedList<>();
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        Path source = Path.of(sourceDirectory);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source, "*.jpg")) {
            Iterator<Path> i = stream.iterator();
            AlbumBuffer albumBuffer = new AlbumBuffer();
            ProducerThread pr;
            for (int j = 0; j < 3; ++j) {
                pr = new ProducerThread("pr-" + j, albumBuffer, i);
                producerThreads.add(pr);
                pr.start();
            }
            ConsumerThread ct;
            for (int j = 0; j < 5; ++j) {
                ct = new ConsumerThread("cs-" + j, albumBuffer, outputDirectory);
                consumerThreads.add(ct);
                ct.start();
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
