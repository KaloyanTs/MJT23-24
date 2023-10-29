package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;

public class Resource implements Completable {

    private final String name;
    private final ResourceDuration duration;

    private int completionPercentage;

    public Resource(String name, ResourceDuration duration) {
        this.name = name;
        this.duration = duration;
        this.completionPercentage = 0;
    }

    @Override
    public boolean isCompleted() {
        return completionPercentage == 100;
    }

    @Override
    public int getCompletionPercentage() {
        return completionPercentage;
    }

    /**
     * Returns the resource name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the total duration of the resource.
     */
    public ResourceDuration getDuration() {
        return duration;
    }

    /**
     * Marks the resource as completed.
     */
    public void complete() {
        completionPercentage = 100;
    }
}
