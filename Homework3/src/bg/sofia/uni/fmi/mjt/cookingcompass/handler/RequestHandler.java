package bg.sofia.uni.fmi.mjt.cookingcompass.handler;

import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.List;

public abstract class RequestHandler {

    protected static final Gson GSON;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * @param keywords keywords by which results are retrieved from some database
     * @return RequestResponse describing result of the query
     */
    public abstract RequestResponse getByKeywords(String... keywords);

    <T> List<T> convertResponse(RequestResponse response, Class<T> tClass) {
        JsonElement element = GSON.fromJson(response.bodyJson(), JsonElement.class);
        return element.getAsJsonObject().get("hits")
            .getAsJsonArray().asList().stream()
            .map(x -> GSON.fromJson(x.getAsJsonObject().get("recipe")
                .getAsJsonObject(), tClass))
            .toList();
    }
}
