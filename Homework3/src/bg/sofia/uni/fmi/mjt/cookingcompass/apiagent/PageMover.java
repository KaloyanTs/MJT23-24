package bg.sofia.uni.fmi.mjt.cookingcompass.apiagent;

import com.google.gson.JsonElement;

public interface PageMover {

    String getNextPage(JsonElement element);
}
