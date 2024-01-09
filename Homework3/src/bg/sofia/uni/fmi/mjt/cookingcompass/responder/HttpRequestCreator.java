package bg.sofia.uni.fmi.mjt.cookingcompass.responder;

import bg.sofia.uni.fmi.mjt.cookingcompass.WebAPIRepresentor;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpRequestCreator implements RequestCreator {

    WebAPIRepresentor representor;

    public HttpRequestCreator(WebAPIRepresentor representor) {
        this.representor = representor;
    }

    @Override
    public String makeRequest(List<String> keywords) {
        Map<String, List<String>> grouped = representor.groupByKeywordGroup(keywords);

        StringBuilder q = new StringBuilder();

        for (String word :
            keywords.stream().filter(
                    keyword -> representor.getKeywordsOfGroup("q").contains(keyword))
                .toList()) {
            q.append("q=").append(word).append("&");
        }
        String app_id_key = "app_id=4abc3395&app_key=b5cd0fc2f0bd828ebc6f6796cd5fe5e1&";

        StringBuilder health = new StringBuilder();

        for (String word :
            keywords.stream().filter(representor.getKeywordsOfGroup("healthLabels")::contains).toList()) {
            health.append("healthLabels=").append(word).append("&");
        }

        StringBuilder dishType = new StringBuilder();

        for (String word : keywords.stream().filter("dishType"::contains).toList()) {
            dishType.append("dishType=").append(word).append("&");
        }

        //String request = representor.getUrl() + "?" +

        return "https://api.edamam.com/api/recipes/v2" + "?" + "type=public&"
            + q
            + "app_id=" + representor.getAppId()
            + "app_key=" + representor.getAppKey()
            + health
            + dishType
            + "field=label" + "&"
            + "field=totalWeight" + "&"
            + "field=dietLabels" + "&"
            + "field=healthLabels" + "&"
            + "field=cuisineType" + "&"
            + "field=mealType" + "&"
            + "field=dishType" + "&"
            + "field=ingredientLines";
    }
}
