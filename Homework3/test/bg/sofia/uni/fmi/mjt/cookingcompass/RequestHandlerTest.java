package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.EdamamPageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.PageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.WebAPIAgent;
import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.EdamamRequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class RequestHandlerTest {

    private final RequestHandler handler = mock();
    private final EdamamClient client = new EdamamClient(handler);

    private static Gson gson;

    @BeforeAll
    static void init() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Test
    void testMockingRequestHandler() {
        Recipe r1 = new Recipe("some dish", List.of("balanced"), List.of("vegetarian", "alcohol-free"), 100,
            List.of("desert"), List.of("breakfast"), List.of("soup"), List.of("*Throw into the fridge\n*don't move"));

        String expectedJson = gson.toJson(List.of(r1));
        Mockito.when(handler.getByKeywords("chicken", "vegetarian")).thenReturn(
            new RequestResponse(0,
                expectedJson)
        );

        String emptyJson = gson.toJson(new Object());

        Mockito.when(handler.getByKeywords("chickan", "vegetarian")).thenReturn(
            new RequestResponse(1,
                emptyJson)
        );

        assertEquals(0, client.makeRequest("chicken", "vegetarian").statusCode());
        assertEquals(expectedJson, client.makeRequest("chicken", "vegetarian").bodyJson());

        assertNotNull(client.makeRequest("chickan", "vegetarian"));
        assertEquals(emptyJson, client.makeRequest("chickan", "vegetarian").bodyJson());

        assertEquals(expectedJson, client.makeRequest("vegetarian", "chicken").bodyJson());

    }

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

    @Test
    void testWebAPIAgent() {
        String appId = "id";
        String appKey = "key";
        String url = "url";
        Set<String> groupBig = Set.of("A", "B", "C");
        Set<String> groupSmall = Set.of("a", "b", "c");
        Map<String, Set<String>> keywordsGrouped = new HashMap<>();
        keywordsGrouped.put("big", groupBig);
        keywordsGrouped.put("small", groupSmall);

        WebAPIAgent agent = new WebAPIAgent(url, appId, appKey, keywordsGrouped);

        assertIterableEquals(groupBig, agent.getKeywordsOfGroup("big"));
        assertIterableEquals(groupSmall, agent.getKeywordsOfGroup("small"));

        String[] keywords = {"a", "B", "d", "D", "c"};

        Map<String, List<String>> map = agent.groupByKeywordGroup(keywords);

        for (Map.Entry<String, List<String>> group : map.entrySet()) {
            switch (group.getKey()) {
                case "big" -> {
                    for (String v : group.getValue()) {
                        assertTrue(groupBig.contains(v));
                    }
                }
                case "small" -> {
                    for (String v : group.getValue()) {
                        assertTrue(groupSmall.contains(v));
                    }
                }
                case "q" -> assertIterableEquals(
                    Arrays.stream(keywords)
                        .filter(x -> !(groupBig.contains(x) || groupSmall.contains(x))).toList(),
                    group.getValue()
                );
                default -> fail();
            }
        }
    }

    @Test
    void testEdamamPageMover() {
        PageMover pageMover = new EdamamPageMover(new GsonBuilder().setPrettyPrinting().create());

        String json = """
            {
              "from": 1,
              "to": 20,
              "count": 25,
              "_links": {
                "next": {
                  "href": "next page",
                  "title": "Next page"
                }
              }
            }""";

        assertEquals("next page", pageMover.getNextPage(gson.fromJson(json, JsonElement.class)));
    }

    @Test
    void testEdamamClientCreation() {
        EdamamClient client = new EdamamClient("id", "key");
    }

    @Test
    void testGetRecipeFromRecipe() {
        Recipe r1 = new Recipe("some dish", List.of("balanced"), List.of("vegetarian", "alcohol-free"), 100,
            List.of("desert"), List.of("breakfast"), List.of("soup"), List.of("*Throw into the fridge", "*don't move"));

        assertEquals("*Throw into the fridge\n*don't move", r1.getRecipe());
    }

    @Test
    void testEdamamRequestCreation() {
        String appId = "id";
        String appKey = "key";
        String url = "https://test/test/";
        Set<String> groupBig = Set.of("A", "B", "C");
        Set<String> groupSmall = Set.of("a", "b", "c");
        Map<String, Set<String>> keywordsGrouped = new HashMap<>();
        keywordsGrouped.put("healthLabels", groupBig);
        keywordsGrouped.put("dishType", groupSmall);

        WebAPIAgent agent = new WebAPIAgent(url, appId, appKey, keywordsGrouped);
        EdamamRequestCreator requestCreator = new EdamamRequestCreator(agent);

        String[] keywords = {"a", "B", "d", "D", "c"};

        HttpRequest request = requestCreator.makeRequest(keywords);

        assertEquals(
            "https://test/test/?type=public&q=d%2C%20D&app_id=id&app_key=key&health=B&dishType=a&dishType=c&field=label&field=totalWeight&field=dietLabels&field=healthLabels&field=cuisineType&field=mealType&field=dishType&field=ingredientLines",
            request.uri().toString());
    }

}
