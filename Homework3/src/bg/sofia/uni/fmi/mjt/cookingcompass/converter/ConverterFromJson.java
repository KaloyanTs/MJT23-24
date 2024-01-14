package bg.sofia.uni.fmi.mjt.cookingcompass.converter;

import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ConverterFromJson {

    protected static final Gson GSON;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    private static class ConverterFromJsonHelper {
        private static final ConverterFromJson INSTANCE = new ConverterFromJson();
    }

    public static ConverterFromJson getInstance() {
        return ConverterFromJsonHelper.INSTANCE;
    }

    public <T> List<T> convertResponse(RequestResponse response, Class<T> tClass) {
        if (response.statusCode() != 0) {
            throw new IllegalArgumentException("Error in response prevents conversion to " + tClass.getTypeName());
        }
        return response.resultJson().stream().map(x -> GSON.fromJson(x.getAsJsonObject(), tClass)).toList();
    }
}
