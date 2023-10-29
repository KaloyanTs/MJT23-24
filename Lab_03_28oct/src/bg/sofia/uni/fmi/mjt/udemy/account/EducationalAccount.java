package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;

public class EducationalAccount extends AccountBase {

    private int discountCounter;

    public EducationalAccount(String username, double balance) {
        super(username, balance);
        type = AccountType.EDUCATION;
        discountCounter = 0;
    }

    private boolean last5Rule() {
        if (courseCount < 5) return false;
        return (courses[courseCount - 1].isCompleted() &&
                courses[courseCount - 2].isCompleted() &&
                courses[courseCount - 3].isCompleted() &&
                courses[courseCount - 4].isCompleted() &&
                courses[courseCount - 5].isCompleted() &&
                (courses[courseCount - 1].getGrade() +
                        courses[courseCount - 2].getGrade() +
                        courses[courseCount - 3].getGrade() +
                        courses[courseCount - 4].getGrade() +
                        courses[courseCount - 5].getGrade()) / 5.0 >= 4.5);
    }

    public void buyCourse(Course course) throws CourseAlreadyPurchasedException,
            InsufficientBalanceException,
            MaxCourseCapacityReachedException {
        if (discountCounter >= 5 && last5Rule()) {
            discountCounter = 0;
            buyCourseWithDiscount(course, true);
        } else {
            buyCourseWithDiscount(course, false);
            ++discountCounter;
        }
    }
}
