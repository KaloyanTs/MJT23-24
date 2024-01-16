package bg.sofia.uni.fmi.mjt.cookingcompass.api;

import bg.sofia.uni.fmi.mjt.cookingcompass.page.PageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.Request;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.RequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawRequestResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.retriever.DataRetriever;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;

public class APIClientTest {

    @Test
    void testRetrieveAllData() {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        RawRequestResponse requestResponse = new RawRequestResponse(0, """
            {
                "data": 1,
                "next page": "page 2"
            }""");
        RawRequestResponse request2Response = new RawRequestResponse(0, """
            {
                "data": 2,
                "next page": "no next page available"
            }""");

        Request getRequest = new Request("get");
        Request page2Request = new Request("page 2");

        String[] keywords = {"get"};

        PageMover mover = mock();
        Mockito.when(mover.getNextPage(requestResponse))
            .thenReturn("page 2");
        Mockito.when(mover.getNextPage(request2Response))
            .thenReturn("");

        RequestCreator requestCreator = mock();
        Mockito.when(requestCreator.makeRequest(keywords)).thenReturn(getRequest);
        Mockito.when(requestCreator.makeRequest("page 2")).thenReturn(page2Request);

        DataRetriever dataRetriever = Mockito.mock(
            DataRetriever.class, Mockito.withSettings()
                .useConstructor(requestCreator)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );
        Mockito.when(dataRetriever.retrieveAllData("a", "b", "c"))
            .thenReturn(new RequestResponse(0, List.of()));

        RequestResponse allData = dataRetriever.retrieveAllData("c", "b", "a");
        assertIterableEquals(
            List.of(gson.fromJson("{\"data\":1}", JsonElement.class)),
            allData.resultJson()
        );
    }
}
