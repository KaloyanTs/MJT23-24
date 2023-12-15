package bg.sofia.uni.fmi.mjt.photoalbum.threads;

import bg.sofia.uni.fmi.mjt.photoalbum.AlbumBuffer;
import bg.sofia.uni.fmi.mjt.photoalbum.Image;

import java.nio.file.Path;

public class ProducerThread extends Thread {
    private final Path path;
    private final AlbumBuffer albumBuffer;
    private final String name;

    public ProducerThread(String name, AlbumBuffer albumBuffer, Path path) {
        this.name = name;
        this.albumBuffer = albumBuffer;
        this.path = path;
    }

    @Override
    public void run() {
        System.out.println(STR."Producer \{name} started...");
        Image img = Image.loadImage(path);
        synchronized (albumBuffer) {
            albumBuffer.addJob(img);
            albumBuffer.notify();
        }
    }
}
