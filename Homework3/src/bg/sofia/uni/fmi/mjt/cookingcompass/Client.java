package bg.sofia.uni.fmi.mjt.cookingcompass;

import java.util.List;

public class Client {

    public static void main(String[] args) {

        HTTPRecipeRequestResponder recipeRequestResponder = new HTTPRecipeRequestResponder();
        RequestHandler requestHandler = new RequestHandler(recipeRequestResponder);
        System.out.println(
            requestHandler.byKeywords(List.of("tomato", "biscuits%20and%20cookies", "dairy-free")).size());

    }
}
