package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.WebAPIAgent;
import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.WebRequestHandler;
import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.HttpRequestCreator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EdamamClient {

    private static Set<String> getHealthLabel() {
        Set<String> healthLabels = new HashSet<>();
        healthLabels.add("alcohol-cocktail");
        healthLabels.add("alcohol-free");
        healthLabels.add("celery-free");
        healthLabels.add("cean-free");
        healthLabels.add("dairy-free");
        healthLabels.add("DASH");
        healthLabels.add("egg-free");
        healthLabels.add("fish-free");
        healthLabels.add("fodmap-free");
        healthLabels.add("gluten-free");
        healthLabels.add("immuno-supportive");
        healthLabels.add("keto-friendly");
        healthLabels.add("kidney-friendly");
        healthLabels.add("kosher");
        healthLabels.add("low-potassium");
        healthLabels.add("low-sugar");
        healthLabels.add("lupine-free");
        healthLabels.add("Mediterranean");
        healthLabels.add("mollusk-free");
        healthLabels.add("mustard-free");
        healthLabels.add("No-oil-added");
        healthLabels.add("paleo");
        healthLabels.add("peanut-free");
        healthLabels.add("pecatarian");
        healthLabels.add("pork-free");
        healthLabels.add("red-meat-free");
        healthLabels.add("sesame-free");
        healthLabels.add("shellfish-free");
        healthLabels.add("soy-free");
        healthLabels.add("sugar-conscious");
        healthLabels.add("sulfite-free");
        healthLabels.add("tree-nut-free");
        healthLabels.add("vegan");
        healthLabels.add("vegetarian");
        healthLabels.add("wheat-free");
        return healthLabels;
    }

    private static Set<String> getDishType() {
        Set<String> dishType = new HashSet<>();
        dishType.add("alcohol%20cocktail");
        dishType.add("biscuits%20and%20cookies");
        dishType.add("bread");
        dishType.add("cereals");
        dishType.add("condiments%20and%20sauces");
        dishType.add("desserts");
        dishType.add("drinks");
        dishType.add("egg");
        dishType.add("ice%20cream%20and%20custard");
        dishType.add("main%20course");
        dishType.add("pancake");
        dishType.add("pasta");
        dishType.add("pastry");
        dishType.add("pies%20and%20tarts");
        dishType.add("pizza");
        dishType.add("preps");
        dishType.add("preserve");
        dishType.add("salad");
        dishType.add("sandwiches");
        dishType.add("seafood");
        dishType.add("side%20dish");
        dishType.add("soup");
        dishType.add("special%20occasions");
        dishType.add("starter");
        dishType.add("sweets");
        return dishType;
    }

    public static void main(String[] args) {

        Set<String> healthLabels = getHealthLabel();
        Set<String> dishType = getDishType();
        Map<String, Set<String>> keywordsGrouped = new HashMap<>();
        keywordsGrouped.put("healthLabels", healthLabels);
        keywordsGrouped.put("dishType", dishType);

        WebAPIAgent agent = new WebAPIAgent("https://api.edamam.com/api/recipes/v2", "4abc3395",
            "b5cd0fc2f0bd828ebc6f6796cd5fe5e1", keywordsGrouped);

        HttpRequestCreator requestCreator = new HttpRequestCreator(agent);

        WebRequestHandler<List<Recipe>> requestHandler = new WebRequestHandler<>(agent);

        List<Recipe> vegieChickenSoups = requestHandler.getByKeywords("chicken", "vegetarian", "soup", "tomato");

        System.out.println(vegieChickenSoups.getFirst().getRecipe());
        System.out.println(vegieChickenSoups.size());

    }
}
