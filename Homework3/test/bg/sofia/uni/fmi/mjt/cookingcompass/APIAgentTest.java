package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.api.APIAgent;
import bg.sofia.uni.fmi.mjt.cookingcompass.page.EdamamPageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.page.PageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.Request;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.RequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.retriever.DataRetriever;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;

public class APIAgentTest {

    @BeforeAll
    static void initGson() {
        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    @Test
    void testWithoutPages() {

        Request request = new Request("give me nothing");

        RequestCreator requestCreator = mock();
        Mockito.when(requestCreator.makeRequest("empty", "empty"))
            .thenReturn(request);

        DataRetriever dataRetriever = mock();
        Mockito.when(dataRetriever.retrieveAllData("empty", "empty"))
            .thenReturn(new RequestResponse(0, List.of()));

        APIAgent agent = new APIAgent(dataRetriever);

        List<Integer> actual = agent.byKeywords(Integer.class, "empty", "empty");
        assertIterableEquals(actual, List.of());
    }

    @Disabled
    @Test
    void testWebAPIAgent() {
//        String appId = "id";
//        String appKey = "key";
//        String url = "url";
//        Set<String> groupBig = Set.of("A", "B", "C");
//        Set<String> groupSmall = Set.of("a", "b", "c");
//        Map<String, Set<String>> keywordsGrouped = new HashMap<>();
//        keywordsGrouped.put("big", groupBig);
//        keywordsGrouped.put("small", groupSmall);
//
//        WebAPIAgent agent = new WebAPIAgent(url, appId, appKey, keywordsGrouped);
//
//        assertIterableEquals(groupBig, agent.getKeywordsOfGroup("big"));
//        assertIterableEquals(groupSmall, agent.getKeywordsOfGroup("small"));
//
//        String[] keywords = {"a", "B", "d", "D", "c"};
//
//        Map<String, List<String>> map = agent.groupByKeywordGroup(keywords);
//
//        for (Map.Entry<String, List<String>> group : map.entrySet()) {
//            switch (group.getKey()) {
//                case "big" -> {
//                    for (String v : group.getValue()) {
//                        assertTrue(groupBig.contains(v));
//                    }
//                }
//                case "small" -> {
//                    for (String v : group.getValue()) {
//                        assertTrue(groupSmall.contains(v));
//                    }
//                }
//                case "q" -> assertIterableEquals(
//                    Arrays.stream(keywords)
//                        .filter(x -> !(groupBig.contains(x) || groupSmall.contains(x))).toList(),
//                    group.getValue()
//                );
//                default -> fail();
//            }
//        }
    }

    @Disabled
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

        //assertEquals("next page", pageMover.getNextPage(gson.fromJson(json, JsonElement.class)));
    }
}
