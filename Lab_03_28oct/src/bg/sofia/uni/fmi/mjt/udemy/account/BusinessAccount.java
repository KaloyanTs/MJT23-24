package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;

public class BusinessAccount extends AccountBase {

    private final Category[] allowed;

    public BusinessAccount(String username, double balance, Category[] allowed) {
        super(username, balance);
        type = AccountType.BUSINESS;
        this.allowed = allowed;
    }

    private boolean isFromAllowedCategory(Course course) {
        for (Category c : allowed)
            if (c == course.getCategory()) {
                return true;
            }
        return false;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (!isFromAllowedCategory(course)) {
            throw new IllegalArgumentException();
        }
        super.buyCourseWithDiscount(course,true);
    }
}
