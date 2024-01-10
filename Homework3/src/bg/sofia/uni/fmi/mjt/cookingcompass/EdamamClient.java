package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.EdamamPageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.EdamamRequestHandler;
import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.PageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.WebAPIAgent;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EdamamClient {

    String appId;
    String appKey;
    RequestHandler handler;

    private static Set<String> getHealthLabel() {
        Set<String> healthLabels =
            Set.of("alcohol-cocktail", "alcohol-free", "celery-free", "cean-free", "dairy-free", "DASH", "egg-free",
                "fish-free", "fodmap-free", "gluten-free", "immuno-supportive", "keto-friendly", "kidney-friendly",
                "kosher", "low-potassium", "low-sugar", "lupine-free", "Mediterranean", "mollusk-free", "mustard-free",
                "No-oil-added", "paleo", "peanut-free", "pecatarian", "pork-free", "red-meat-free", "sesame-free",
                "shellfish-free", "soy-free", "sugar-conscious", "sulfite-free", "tree-nut-free", "vegan", "vegetarian",
                "wheat-free");
        return healthLabels;
    }

    private static Set<String> getDishType() {
        Set<String> dishType =
            Set.of("alcohol%20cocktail", "biscuits%20and%20cookies", "bread", "cereals", "condiments%20and%20sauces",
                "desserts", "drinks", "egg", "ice%20cream%20and%20custard", "main%20course", "pancake", "pasta",
                "pastry", "pies%20and%20tarts", "pizza", "preps", "preserve", "salad", "sandwiches", "seafood",
                "side%20dish", "soup", "special%20occasions", "starter", "sweets");
        return dishType;
    }

    public RequestResponse makeRequest(String... keywords) {
        Arrays.sort(keywords);
        return handler.getByKeywords(keywords);
    }

    public EdamamClient(RequestHandler handler) {
        this.appId = null;
        this.appKey = null;
        this.handler = handler;
    }

    public EdamamClient(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
        Set<String> healthLabels = getHealthLabel();
        Set<String> dishType = getDishType();
        Map<String, Set<String>> keywordsGrouped = new HashMap<>();
        keywordsGrouped.put("healthLabels", healthLabels);
        keywordsGrouped.put("dishType", dishType);

        WebAPIAgent agent = new WebAPIAgent("https://api.edamam.com/api/recipes/v2", appId,
            appKey, keywordsGrouped);

        PageMover pageMover = new EdamamPageMover(new GsonBuilder().setPrettyPrinting().create());

        handler = new EdamamRequestHandler(agent, pageMover);
    }

//    public static void mainBefore(String[] args) {
//        RequestResponse veggieChickenSoupsJson =
//            handler.getByKeywords("chicken", "vegetarian", "soup", "tomato");
//
//        System.out.println(veggieChickenSoupsJson.statusCode());
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//        JsonElement element = gson.fromJson(veggieChickenSoupsJson.bodyJson(), JsonElement.class);
//
//        List<Recipe> veggieChickenSoutp = element.getAsJsonArray().asList().stream().map(x -> gson.fromJson(x,
//            Recipe.class)).toList();
//
//        System.out.println(veggieChickenSoupsJson.bodyJson());
//
//    }
}
