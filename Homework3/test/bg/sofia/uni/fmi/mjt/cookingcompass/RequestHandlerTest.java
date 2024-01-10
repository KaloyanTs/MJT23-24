package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class RequestHandlerTest {

    private final RequestHandler handler = mock();
    private final EdamamClient client = new EdamamClient(handler);

    private static Gson gson;

    @BeforeAll
    static void init() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Test
    void testMockingRequestHandler() {
        Recipe r1 = new Recipe("some dish", List.of("balanced"), List.of("vegetarian", "alcohol-free"), 100,
            List.of("desert"), List.of("breakfast"), List.of("soup"), List.of("*Throw into the fridge\n*don't move"));

        String expectedJson = gson.toJson(List.of(r1));
        Mockito.when(handler.getByKeywords("chicken", "vegetarian")).thenReturn(
            new RequestResponse(0,
                expectedJson)
        );

        String emptyJson = gson.toJson(new Object());

        Mockito.when(handler.getByKeywords("chickan", "vegetarian")).thenReturn(
            new RequestResponse(1,
                emptyJson)
        );

        assertEquals(0, client.makeRequest("chicken", "vegetarian").statusCode());
        assertEquals(expectedJson, client.makeRequest("chicken", "vegetarian").bodyJson());

        assertNotNull(client.makeRequest("chickan", "vegetarian"));
        assertEquals(emptyJson, client.makeRequest("chickan", "vegetarian").bodyJson());

        assertEquals(expectedJson, client.makeRequest("vegetarian", "chicken").bodyJson());

    }

}
