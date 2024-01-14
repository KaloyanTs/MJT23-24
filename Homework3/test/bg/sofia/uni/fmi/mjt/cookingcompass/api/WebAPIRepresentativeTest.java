package bg.sofia.uni.fmi.mjt.cookingcompass.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class WebAPIRepresentativeTest {

    private Set<String> groupBig;
    private Set<String> groupSmall;
    private WebAPIRepresentative apiRepresentative;

    @BeforeEach
    void init() {
        String appId = "id";
        String appKey = "key";
        String url = "url";
        groupBig = Set.of("A", "B", "C");
        groupSmall = Set.of("a", "b", "c");
        Map<String, Set<String>> keywordsGrouped = new HashMap<>();
        keywordsGrouped.put("big", groupBig);
        keywordsGrouped.put("small", groupSmall);

        apiRepresentative = new WebAPIRepresentative(url, appId, appKey, keywordsGrouped);
    }

    @Test
    void testGetKeywordsOfGroupNotKnown() {
        assertThrows(IllegalArgumentException.class, () -> apiRepresentative.getKeywordsOfGroup("random"));
    }

    @Test
    void testCredentials() {
        assertEquals("id", apiRepresentative.getAppId());
        assertEquals("key", apiRepresentative.getAppKey());
        assertEquals("url", apiRepresentative.getUrl());
    }

    @Test
    void testGroupByKeywordGroup() {
        assertIterableEquals(groupBig, apiRepresentative.getKeywordsOfGroup("big"));
        assertIterableEquals(groupSmall, apiRepresentative.getKeywordsOfGroup("small"));

        String[] keywords = {"a", "B", "d", "D", "c"};

        Map<String, List<String>> map = apiRepresentative.groupByKeywordGroup(keywords);

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
