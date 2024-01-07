package bg.sofia.uni.fmi.mjt.cookingcompass;

import java.util.List;

public class Client {

    public static void main(String[] args) {

        for (int i = 0; i < 1000; ++i) {
            System.out.println(
                RequestHandler.byKeywords(List.of("tomato", "biscuits%20and%20cookies", "dairy-free")).size());
        }

    }
}
