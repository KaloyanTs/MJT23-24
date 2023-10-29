package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public class Course implements Completable, Purchasable {

    private final String name;
    private final String description;
    private final double price;
    private final Resource[] content;
    private final Category category;

    private final CourseDuration totalTime;

    private double grade;

    boolean purchased;

    {
        this.purchased = false;
        grade = 0;
    }

    public Course(String name, String description, double price, Resource[] content, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.content = content;
        this.category = category;
        totalTime = CourseDuration.of(content);
    }

    /**
     * Returns the name of the course.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the course.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the price of the course.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the category of the course.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the content of the course.
     */
    public Resource[] getContent() {
        return content;
    }

    /**
     * Returns the total duration of the course.
     */
    public CourseDuration getTotalTime() {
        return totalTime;
    }

    /**
     * Completes a resource from the course.
     *
     * @param resourceToComplete the resource which will be completed.
     * @throws ResourceNotFoundException if the resource could not be found in the course.
     */
    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException {
        for (Resource r : content)
            if (r == resourceToComplete) {
                r.complete();
                return;
            }
        throw new ResourceNotFoundException();
    }

    public void setGrade(double grade) throws CourseNotCompletedException {
        if (!isCompleted()) {
            throw new CourseNotCompletedException();
        }
        this.grade = grade;
    }

    public double getGrade() {
        return grade;
    }

    @Override
    public boolean isCompleted() {
        return getCompletionPercentage() == 100;
    }

    @Override
    public int getCompletionPercentage() {
        if (content.length == 0)
            return 100;
        int sum = 0;
        for (Resource r : content)
            sum += r.getCompletionPercentage();
        return (int) Math.round((double) sum / content.length);
    }

    @Override
    public void purchase() {
        purchased = true;
    }

    @Override
    public boolean isPurchased() {
        return purchased;
    }
}
