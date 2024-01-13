package bg.sofia.uni.fmi.mjt.cookingcompass.retriever;

import bg.sofia.uni.fmi.mjt.cookingcompass.page.PageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.Request;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawRequestResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class PagedDataRetrieverTest {

    @Test
    void testRetrieveAllData() {

        RawRequestResponse requestResponse = new RawRequestResponse(0, """
            {
                "data": 1,
                "next page": "page 2"
            }""");
        RawRequestResponse request2Response = new RawRequestResponse(0, """
            {
                "data": 2,
                "next page": "page 3"
            }""");
        RawRequestResponse request3Response = new RawRequestResponse(2, """
            {
                "data": 3,
                "next page": "page 4"
            }""");
        RawRequestResponse request4Response = new RawRequestResponse(0, """
            {
                "data": 4,
                "next page": ""
            }""");

        PageMover mover = mock();
        Mockito.when(mover.getNextPage(requestResponse))
            .thenReturn("page 2");
        Mockito.when(mover.getNextPage(request2Response))
            .thenReturn("page 3");
        Mockito.when(mover.getNextPage(request3Response))
            .thenReturn("page 4");

        PagedDataRetriever dataRetriever = mock();
        Mockito.when(dataRetriever.retrieveData(new Request("get")))
            .thenReturn(requestResponse);
        Mockito.when(dataRetriever.retrieveData(new Request("page 2")))
            .thenReturn(request2Response);
        Mockito.when(dataRetriever.retrieveData(new Request("page 3")))
            .thenReturn(request3Response);
        Mockito.when(dataRetriever.retrieveData(new Request("page 4")))
            .thenReturn(request4Response);

        RequestResponse allData = dataRetriever.retrieveAllData("get");
    }
}
