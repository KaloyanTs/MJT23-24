package bg.sofia.uni.fmi.mjt.cookingcompass.api;

import bg.sofia.uni.fmi.mjt.cookingcompass.client.APIClient;
import bg.sofia.uni.fmi.mjt.cookingcompass.exception.BadCredentialsException;
import bg.sofia.uni.fmi.mjt.cookingcompass.exception.NotAuthorizedException;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.RequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.retriever.DataRetriever;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class APIClientTest {

    @Test
    void testMakeRequestWithoutCredentials() {
        RequestCreator requestCreator = mock();

        DataRetriever dataRetriever = Mockito.mock(
            DataRetriever.class, Mockito.withSettings()
                .useConstructor(0, requestCreator)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );
        APIClient apiClient = Mockito.mock(APIClient.class,
            Mockito.withSettings()
                .useConstructor(dataRetriever)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );

        assertFalse(apiClient.doesHaveCredentials());
        assertThrows(NotAuthorizedException.class, apiClient::makeRequest);
    }

    @Test
    void testRetrieveAllData() throws BadCredentialsException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        RequestCreator requestCreator = mock();

        DataRetriever dataRetriever = Mockito.mock(
            DataRetriever.class, Mockito.withSettings()
                .useConstructor(0, requestCreator)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );
        String[] keywordsArray = {"a", "b", "c"};
        List<JsonElement> list = List.of(
            gson.fromJson("{\"data\":1}", JsonElement.class),
            gson.fromJson("{\"data\":2}", JsonElement.class)
        );
        Mockito.when(dataRetriever.retrieveAllData(keywordsArray))
            .thenReturn(new RequestResponse(true, 0, list));

        APIClient apiClient = Mockito.mock(
            APIClient.class, Mockito.withSettings()
                .useConstructor(dataRetriever)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );
        String[] credentials = {};

        apiClient.user(credentials);
        assertIterableEquals(list, apiClient.makeRequest(keywordsArray).resultJson());
    }
}
