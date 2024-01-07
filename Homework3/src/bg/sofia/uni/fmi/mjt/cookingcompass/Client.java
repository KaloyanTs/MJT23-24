package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        System.out.println(RequestHandler.byKeywords(List.of("salad", "chicken", "alcohol-free")).size());

    }
}
