package bg.sofia.uni.fmi.mjt.cookingcompass.apiagent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class EdamamPageMover implements PageMover {

    private final Gson gson;

    public EdamamPageMover(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String getNextPage(JsonElement element) {
        String nextPage;
        JsonElement links = element.getAsJsonObject().get("_links");
        if (links.getAsJsonObject().isEmpty()) {
            nextPage = "";
        } else {
            nextPage =
                gson.fromJson(
                    links.getAsJsonObject().get("next").getAsJsonObject().get(
                        "href"), String.class);
        }
        return nextPage;
    }
}
