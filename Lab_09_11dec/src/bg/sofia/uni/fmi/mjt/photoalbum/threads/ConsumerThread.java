package bg.sofia.uni.fmi.mjt.photoalbum.threads;

import bg.sofia.uni.fmi.mjt.photoalbum.AlbumBuffer;
import bg.sofia.uni.fmi.mjt.photoalbum.Image;

import java.nio.file.Path;

public class ConsumerThread extends Thread {

    private final AlbumBuffer albumBuffer;
    String outputDirectory;
    String name;

    public ConsumerThread(String name, AlbumBuffer albumBuffer, String outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.albumBuffer = albumBuffer;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Consumer " + name + " started...");
        Image img;
        while (true) {
            synchronized (albumBuffer) {
                while (albumBuffer.isEmpty()) {
                    try {
                        if (albumBuffer.isJobDone()) {
                            return;
                        }
                        albumBuffer.notify();
                        albumBuffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                img = albumBuffer.getJob();
                albumBuffer.notify();
            }
            Image.proceedImage(img, Path.of(outputDirectory + "\\" + img.getName()));
        }
    }
}