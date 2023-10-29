package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes) {

    public CourseDuration {
        if (hours < 0 || hours > 24 || minutes > 60 || minutes < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static CourseDuration of(Resource[] content) {
        int hours;
        int minutes = 0;
        for (Resource r : content)
            minutes += r.getDuration().minutes();
        hours = minutes / 60;
        minutes %= 60;
        return new CourseDuration(hours, minutes);
    }

    public boolean isLonger(CourseDuration cD) {
        return (60 * hours + minutes) > (60 * cD.hours + minutes);
    }
}
