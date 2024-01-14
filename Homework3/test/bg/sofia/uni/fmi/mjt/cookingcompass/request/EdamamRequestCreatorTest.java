package bg.sofia.uni.fmi.mjt.cookingcompass.request;

import bg.sofia.uni.fmi.mjt.cookingcompass.api.WebAPIRepresentative;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdamamRequestCreatorTest {

    @Test
    void testEdamamRequestCreator() {
        Set<String> healthLabels =
            Set.of("alcohol-cocktail", "alcohol-free", "celery-free", "cean-free", "dairy-free", "DASH", "egg-free",
                "fish-free", "fodmap-free", "gluten-free", "immuno-supportive", "keto-friendly", "kidney-friendly",
                "kosher", "low-potassium", "low-sugar", "lupine-free", "Mediterranean", "mollusk-free", "mustard-free",
                "No-oil-added", "paleo", "peanut-free", "pecatarian", "pork-free", "red-meat-free", "sesame-free",
                "shellfish-free", "soy-free", "sugar-conscious", "sulfite-free", "tree-nut-free", "vegan", "vegetarian",
                "wheat-free");

        Set<String> dishType =
            Set.of("alcohol%20cocktail", "biscuits%20and%20cookies", "bread", "cereals", "condiments%20and%20sauces",
                "desserts", "drinks", "egg", "ice%20cream%20and%20custard", "main%20course", "pancake", "pasta",
                "pastry", "pies%20and%20tarts", "pizza", "preps", "preserve", "salad", "sandwiches", "seafood",
                "side%20dish", "soup", "special%20occasions", "starter", "sweets");

        Map<String, Set<String>> keywordsGrouped = new HashMap<>();
        keywordsGrouped.put("healthLabels", healthLabels);
        keywordsGrouped.put("dishType", dishType);

        WebAPIRepresentative edamam = new WebAPIRepresentative(
            "https://api.edamam.com/api/recipes/v2",
            "id",
            "key",
            keywordsGrouped);

        EdamamRequestCreator requestCreator = new EdamamRequestCreator(edamam);
        Request request = requestCreator.makeRequest("tomato", "chicken");
        assertEquals(
            "https://api.edamam.com/api/recipes/v2?type=public&q=tomato%2C%20chicken&app_id=id&app_key=key&field=label&field=totalWeight&field=dietLabels&field=healthLabels&field=cuisineType&field=mealType&field=dishType&field=ingredientLines",
            request.getData());
        request = requestCreator.makeRequest("alcohol-free", "chicken", "vegetarian", "bread");
        assertEquals(
            "https://api.edamam.com/api/recipes/v2?type=public&q=chicken&app_id=id&app_key=key&health=alcohol-free&health=vegetarian&dishType=bread&field=label&field=totalWeight&field=dietLabels&field=healthLabels&field=cuisineType&field=mealType&field=dishType&field=ingredientLines",
            request.getData());
    }
}
