package bg.sofia.uni.fmi.mjt.cookingcompass.page;

import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class EdamamPageMover implements PageMover {

    private final Gson gson;

    public EdamamPageMover(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String getNextPage(RawResponse element) {
        String nextPage;
        JsonElement links = gson.fromJson(element.bodyJson(), JsonElement.class)
            .getAsJsonObject()
            .get("_links");
        if (links == null || links.getAsJsonObject().isEmpty()) {
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
