package bg.sofia.uni.fmi.mjt.cookingcompass.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WebAPIRepresentative {

    private final String url;
    private final String appId;
    private final String appKey;
    private final Map<String, Set<String>> keywordsGrouped;

    public WebAPIRepresentative(String url, String appId, String appKey, Map<String, Set<String>> keywordsGrouped) {
        this.url = url;
        this.appId = appId;
        this.appKey = appKey;
        this.keywordsGrouped = keywordsGrouped;
    }

    private String findGroup(String keyword) {
        for (Map.Entry<String, Set<String>> group : keywordsGrouped.entrySet()) {
            if (group.getValue().contains(keyword)) {
                return group.getKey();
            }
        }
        return "q";
    }

    public Map<String, List<String>> groupByKeywordGroup(String... keywords) {
        return Arrays.stream(keywords).collect(Collectors.groupingBy(this::findGroup));
    }

    public Set<String> getKeywordsOfGroup(String group) {
        Set<String> result = keywordsGrouped.get(group);
        if (result == null) {
            throw new IllegalArgumentException("Given group not maintained by the Web API...");
        }
        return result;
    }

    public String getAppId() {
        return appId;
    }

    public String getUrl() {
        return url;
    }

    public String getAppKey() {
        return appKey;
    }
}
