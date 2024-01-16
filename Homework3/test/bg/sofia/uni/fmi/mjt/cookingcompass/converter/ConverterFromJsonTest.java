package bg.sofia.uni.fmi.mjt.cookingcompass.converter;

import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConverterFromJsonTest {

    record Wrap(String value) {
    }

    private Gson gson;
    List<Wrap> expected;

    @BeforeEach
    void init() {
        gson = new GsonBuilder().setPrettyPrinting().create();

        expected = List.of(
            new Wrap("i:1"),
            new Wrap("i:2"),
            new Wrap("i:3"),
            new Wrap("i:4")
        );
    }

    @Test
    void testConversionGoodResponse() {
        RequestResponse response = new RequestResponse(0, expected.stream().map(x -> gson.fromJson(gson.toJson(x),
            JsonElement.class)).toList());
        List<Wrap> actual = ConverterFromJson.getInstance().convertResponse(response, Wrap.class);
        assertIterableEquals(expected, actual, "Converter must convert list of JSONs to a list of objects properly");
    }

    @Test
    void testConversionBadResponse() {
        RequestResponse response = new RequestResponse(404, expected.stream().map(x -> gson.fromJson(gson.toJson(x),
            JsonElement.class)).toList());
        assertThrows(IllegalArgumentException.class, () -> ConverterFromJson.getInstance().convertResponse(response,
                Wrap.class),
            "Converter must throw IllArgException when status of response is not the one of a successful one");
    }
}
