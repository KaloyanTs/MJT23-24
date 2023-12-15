package bg.sofia.uni.fmi.mjt.photoalbum;

import java.util.LinkedList;
import java.util.Queue;

public class AlbumBuffer {

    private final Queue<Image> queue;
    private int jobCount;

    public AlbumBuffer(int jobCount) {
        queue = new LinkedList<>();
        this.jobCount = jobCount;
    }

    public synchronized boolean isJobDone() {
        return jobCount == 0;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public Image getJob() {
        --jobCount;
        return queue.poll();
    }

    public void addJob(Image img) {
        queue.offer(img);
    }
}
