package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

public class Udemy implements LearningPlatform {

    Course[] courses;
    Account[] accounts;

    public Udemy(Account[] accounts, Course[] courses) {
        this.accounts = accounts;
        this.courses = courses;
    }

    @Override
    public Course findByName(String name) throws CourseNotFoundException {

        for (Course c : courses)
            if (c.getName().equals(name)) {
                return c;
            }

        throw new CourseNotFoundException();
    }

    private static boolean isKeyword(String word) {
        if (word == null || word.isBlank()) {
            return false;
        }
        for (char c : word.toCharArray())
            if (!Character.isAlphabetic(c)) {
                return false;
            }
        return true;
    }

    @Override
    public Course[] findByKeyword(String keyword) {
        if (!isKeyword(keyword)) {
            throw new IllegalArgumentException();
        }
        int size = 0;
        for (Course course : courses)
            if (course.getName().contains(keyword) || course.getDescription().contains(keyword)) {
                ++size;
            }
        Course[] res = new Course[size];
        size = 0;
        for (Course course : courses)
            if (course.getName().contains(keyword) || course.getDescription().contains(keyword)) {
                res[size++] = course;
            }
        return res;
    }

    @Override
    public Course[] getAllCoursesByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException();
        }
        int size = 0;
        for (Course course : courses)
            if (course.getCategory() == category) {
                ++size;
            }
        Course[] res = new Course[size];
        size = 0;
        for (Course course : courses)
            if (course.getCategory() == category) {
                res[size++] = course;
            }
        return res;
    }

    @Override
    public Account getAccount(String name) throws AccountNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException();
        }
        for (Account a : accounts)
            if (a.getUsername().equals(name)) {
                return a;
            }
        throw new AccountNotFoundException();
    }

    @Override
    public Course getLongestCourse() {
        if (courses.length == 0) {
            return null;
        }
        Course maxDcourse = courses[0];
        for (Course a : courses)
            if (a.getTotalTime().isLonger(maxDcourse.getTotalTime())) {
                maxDcourse = a;
            }
        return maxDcourse;
    }

    @Override
    public Course getCheapestByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException();
        }
        Course chCourse = null;
        for (
                Course a : courses)
            if (a.getCategory() == category && (chCourse == null || a.getPrice() < chCourse.getPrice())) {
                chCourse = a;
            }
        return chCourse;
    }
}
