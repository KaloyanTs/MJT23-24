package bg.sofia.uni.fmi.mjt.cookingcompass.response;

import com.google.gson.JsonElement;

import java.util.List;

public record RequestResponse(boolean successful, int statusCode, List<JsonElement> resultJson) {
}
