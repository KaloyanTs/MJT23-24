package bg.sofia.uni.fmi.mjt.cookingcompass.api;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class WebAPIRepresentativeTest {

    @Test
    void testDoesNotHaveCredentials() {
        //WebAPIRepresentative api = new WebAPIRepresentative("https://", "id", "")
    }

    @Test
    void testGroupByKeywordGroup() {
        String appId = "id";
        String appKey = "key";
        String url = "url";
        Set<String> groupBig = Set.of("A", "B", "C");
        Set<String> groupSmall = Set.of("a", "b", "c");
        Map<String, Set<String>> keywordsGrouped = new HashMap<>();
        keywordsGrouped.put("big", groupBig);
        keywordsGrouped.put("small", groupSmall);

        WebAPIRepresentative agent = new WebAPIRepresentative(url, appId, appKey, keywordsGrouped);

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

}
