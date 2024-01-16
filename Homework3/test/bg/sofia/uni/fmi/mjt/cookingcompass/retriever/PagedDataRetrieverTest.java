package bg.sofia.uni.fmi.mjt.cookingcompass.retriever;

import bg.sofia.uni.fmi.mjt.cookingcompass.page.PageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.Request;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.RequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;

public class PagedDataRetrieverTest {

    @Test
    void testRetrieveAllData() {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        RawResponse requestResponse = new RawResponse(0, """
            {
                "data": 1,
                "next page": "page 2"
            }""");
        RequestResponse readyRequestResponse = new RequestResponse(true, 0,
            List.of(gson.fromJson("{\"data\":1}", JsonElement.class)));
        RawResponse request2Response = new RawResponse(0, """
            {
                "data": 2,
                "next page": "no next page available"
            }""");
        RequestResponse readyRequest2Response = new RequestResponse(true, 0,
            List.of(gson.fromJson("{\"data\":2}", JsonElement.class)));

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

        PagedDataRetriever dataRetriever = Mockito.mock(
            PagedDataRetriever.class, Mockito.withSettings()
                .useConstructor(0, mover, requestCreator)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );
        Mockito.when(dataRetriever.retrieveData(getRequest))
            .thenReturn(requestResponse);
        Mockito.when(dataRetriever.retrieveData(page2Request))
            .thenReturn(request2Response);
        Mockito.when(dataRetriever.convertResponse(requestResponse))
            .thenReturn(readyRequestResponse);
        Mockito.when(dataRetriever.convertResponse(request2Response))
            .thenReturn(readyRequest2Response);

        RequestResponse allData = dataRetriever.retrieveAllData("get");
        assertIterableEquals(List.of(
                gson.fromJson("{\"data\":1}", JsonElement.class),
                gson.fromJson("{\"data\":2}", JsonElement.class)),
            allData.resultJson()
        );
    }
}
