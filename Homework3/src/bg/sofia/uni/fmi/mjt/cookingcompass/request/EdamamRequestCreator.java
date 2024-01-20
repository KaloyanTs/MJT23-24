package bg.sofia.uni.fmi.mjt.cookingcompass.request;

import bg.sofia.uni.fmi.mjt.cookingcompass.api.WebAPIRepresentative;

import java.util.List;
import java.util.Map;

public class EdamamRequestCreator extends RequestCreator {

    public EdamamRequestCreator(WebAPIRepresentative edamam) {
        super(edamam);
    }

    private String buildHealthLabels(Map<String, List<String>> grouped) {
        StringBuilder health = new StringBuilder();
        if (grouped.get("healthLabels") != null) {
            for (String word : grouped.get("healthLabels")) {
                health.append("health=").append(word).append("&");
            }
        }
        return health.toString();
    }

    private String buildDishType(Map<String, List<String>> grouped) {
        StringBuilder dishType = new StringBuilder();
        if (grouped.get("dishType") != null) {
            for (String word : grouped.get("dishType")) {
                dishType.append("dishType=").append(word).append("&");
            }
        }
        return dishType.toString();
    }

    private String buildQ(Map<String, List<String>> grouped) {
        StringBuilder q = new StringBuilder();
        for (String word : grouped.get("q")) {
            if (q.isEmpty()) {
                q.append("q=")
                    .append(word);
            } else {
                q.append("%2C%20")
                    .append(word);
            }
        }
        q.append("&");
        return q.toString();
    }

    private String buildVisibleFields(String... fields) {
        StringBuilder res = new StringBuilder();
        for (String field : fields) {
            if (!res.isEmpty()) {
                res.append("&");
            }
            res.append("field=")
                .append(field);
        }
        return res.toString();
    }

    public Request makeRequest(String... keywords) {
        Map<String, List<String>> grouped = apiRepresentative.groupByKeywordGroup(keywords);

        String q = buildQ(grouped);
        String health = buildHealthLabels(grouped);
        String dishType = buildDishType(grouped);

        return new Request(
            apiRepresentative.getUrl() + "?" + "type=public&"
                + q
                + "app_id="
                + apiRepresentative.getAppId() + "&"
                + "app_key="
                + apiRepresentative.getAppKey() + "&"
                + health + dishType
                + buildVisibleFields("label",
                "totalWeight",
                "dietLabels",
                "healthLabels",
                "cuisineType",
                "mealType",
                "dishType",
                "ingredientLines")
        );
    }

    public Request makeRequest(String url) {
        return new Request(url);
    }
}
