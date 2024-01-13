package bg.sofia.uni.fmi.mjt.cookingcompass.request;

import bg.sofia.uni.fmi.mjt.cookingcompass.api.WebAPIRepresentative;

import java.util.List;
import java.util.Map;

public class EdamamRequestCreator extends RequestCreator {

    public EdamamRequestCreator(WebAPIRepresentative edamam) {
        super(edamam);
    }

    public Request makeRequest(String... keywords) {
        Map<String, List<String>> grouped = apiRepresentative.groupByKeywordGroup(keywords);

        StringBuilder q = new StringBuilder();

        for (String word : grouped.get("q")) {
            if (q.isEmpty()) {
                q.append("q=").append(word);
            } else {
                q.append("%2C%20").append(word);
            }
        }
        q.append("&");

        StringBuilder health = new StringBuilder();
        for (String word : grouped.get("healthLabels")) {
            health.append("health=").append(word).append("&");
        }

        StringBuilder dishType = new StringBuilder();
        for (String word : grouped.get("dishType")) {
            dishType.append("dishType=").append(word).append("&");
        }

        return new Request(apiRepresentative.getUrl() + "?" + "type=public&"
            + q
            + "app_id=" + apiRepresentative.getAppId() + "&"
            + "app_key=" + apiRepresentative.getAppKey() + "&"
            + health
            + dishType
            + "field=label" + "&"
            + "field=totalWeight" + "&"
            + "field=dietLabels" + "&"
            + "field=healthLabels" + "&"
            + "field=cuisineType" + "&"
            + "field=mealType" + "&"
            + "field=dishType" + "&"
            + "field=ingredientLines");
    }

    public Request makeRequest(String url) {
        return new Request(url);
    }
}
