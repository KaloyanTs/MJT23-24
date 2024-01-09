package bg.sofia.uni.fmi.mjt.cookingcompass.request;

import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.WebAPIAgent;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HttpRequestCreator {

    //todo gets keywords and returns httpRequest to given Web API

    WebAPIAgent agent;

    public HttpRequestCreator(WebAPIAgent agent) {
        this.agent = agent;
    }

    public HttpRequest makeRequest(String... keywords) {
        Map<String, List<String>> grouped = agent.groupByKeywordGroup(keywords);

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

        try {
            return HttpRequest.newBuilder().uri(
                new URI(agent.getUrl() + "?" + "type=public&"
                    + q
                    + "app_id=" + agent.getAppId() + "&"
                    + "app_key=" + agent.getAppKey() + "&"
                    + health
                    + dishType
                    + "field=label" + "&"
                    + "field=totalWeight" + "&"
                    + "field=dietLabels" + "&"
                    + "field=healthLabels" + "&"
                    + "field=cuisineType" + "&"
                    + "field=mealType" + "&"
                    + "field=dishType" + "&"
                    + "field=ingredientLines"
                )
            ).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Something unexpected occurred...", e);
        }
    }

    public HttpRequest makeRequest(String url) throws URISyntaxException {
        return HttpRequest.newBuilder().uri(new URI(url)).build();
    }
}
