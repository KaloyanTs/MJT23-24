package bg.sofia.uni.fmi.mjt.photoalbum;

import bg.sofia.uni.fmi.mjt.photoalbum.threads.ConsumerThread;
import bg.sofia.uni.fmi.mjt.photoalbum.threads.ProducerThread;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {

    private final int imageProcessorsCount;
    List<ConsumerThread> consumerThreads;
    List<ProducerThread> producerThreads;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
        consumerThreads = new ArrayList<>(imageProcessorsCount);
        producerThreads = new ArrayList<>(imageProcessorsCount);
    }

    private void startConsumers(AlbumBuffer albumBuffer, String outputDirectory) {
        ConsumerThread ct;
        for (int j = 0; j < imageProcessorsCount; ++j) {
            ct = new ConsumerThread("cs-" + j, albumBuffer, outputDirectory);
            consumerThreads.add(ct);
            ct.start();
        }
    }

    private void startProducers(DirectoryStream<Path> stream, AlbumBuffer albumBuffer) {
        ProducerThread pr;
        Iterator<Path> i = stream.iterator();
        int j = 0;
        while (i.hasNext()) {
            pr = new ProducerThread("pr-" + j++, albumBuffer, i.next());
            producerThreads.add(pr);
            pr.start();
        }
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        Path source = Path.of(sourceDirectory);
        try (DirectoryStream<Path> streamCount =
                 Files.newDirectoryStream(source, "{*.jpg, *.jpeg, *.png}");
             DirectoryStream<Path> streamWork =
                 Files.newDirectoryStream(source, "{*.jpg, *.jpeg, *.png}")) {

            Iterator<Path> i = streamCount.iterator();
            int count = 0;
            while (i.hasNext()) {
                i.next();
                ++count;
            }

            AlbumBuffer albumBuffer = new AlbumBuffer(count);

            startConsumers(albumBuffer, outputDirectory);
            startProducers(streamWork, albumBuffer);

            for (ConsumerThread thread : consumerThreads) {
                thread.join();
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Some unexpected error occured...", e);
        }
    }
}
