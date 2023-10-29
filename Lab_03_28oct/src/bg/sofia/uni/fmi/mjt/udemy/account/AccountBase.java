package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;

public abstract class AccountBase implements Account {

    protected static int maxCourses;

    private final String username;
    private double balance;
    protected AccountType type;

    protected Course[] courses;
    protected int courseCount;

    static {
        maxCourses = 100;
    }

    {
        courses = new Course[maxCourses];
        courseCount = 0;
    }

    public AccountBase(String username, double balance) {
        this.username = username;
        this.balance = balance;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void addToBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        balance += amount;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    public void buyCourse(Course course) throws CourseAlreadyPurchasedException,
            InsufficientBalanceException,
            MaxCourseCapacityReachedException {
        buyCourseWithDiscount(course, false);
    }

    protected void buyCourseWithDiscount(Course course, boolean discount) throws CourseAlreadyPurchasedException,
            MaxCourseCapacityReachedException, InsufficientBalanceException {
        if (isPurchased(course)) {
            throw new CourseAlreadyPurchasedException();
        }
        if (courseCount == maxCourses) {
            throw new MaxCourseCapacityReachedException();
        }
        double price;
        if (discount) {
            price = (1.0 - type.getDiscount()) * course.getPrice();
        } else {
            price = course.getPrice();
        }
        if (balance < price) {
            throw new InsufficientBalanceException();
        }

        balance -= price;
        course.purchase();
        courses[courseCount++] = course;
    }

    protected boolean isPurchased(Course course) {
        for (Course c : courses)
            if (c == course) {
                return true;
            }
        return false;
    }

    @Override
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException {
        if (!isPurchased(course)) {
            throw new CourseNotPurchasedException();
        }

        for (Resource r : resourcesToComplete) {
            course.completeResource(r);
        }
    }

    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException {
        if (grade < 2.0 || grade > 6.0) {
            throw new IllegalArgumentException();
        }
        if (!isPurchased(course)) {
            throw new CourseNotPurchasedException();
        }
        for (Resource r : course.getContent())
            if (!r.isCompleted()) {
                throw new CourseNotCompletedException();
            }

        course.setGrade(grade);
    }

    public Course getLeastCompletedCourse() {
        if (courseCount == 0) {
            return null;
        }
        Course least = courses[0];
        for (int i = 0; i < courseCount; i++) {
            if (courses[i].getCompletionPercentage() < least.getCompletionPercentage()) {
                least = courses[i];
            }
        }
        return least;
    }

}
