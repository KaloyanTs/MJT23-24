package bg.sofia.uni.fmi.mjt.cookingcompass.retriever;

import bg.sofia.uni.fmi.mjt.cookingcompass.page.PageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.Request;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.RequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawRequestResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PagedDataRetriever extends DataRetriever {

    private PageMover pageMover;

    public PagedDataRetriever(PageMover pageMover, RequestCreator requestCreator) {
        super(requestCreator);
        this.pageMover = pageMover;
    }

    @Override
    public RequestResponse retrieveAllData(String... keywords) {
        List<JsonElement> list = new ArrayList<>();
        String nextPage;
        Request request = requestCreator.makeRequest(keywords);
        Optional<Integer> statusCode = Optional.empty();
        do {
            RawRequestResponse response = retrieveData(request);
            nextPage = pageMover.getNextPage(response);
            if (statusCode.isEmpty()) {
                statusCode = Optional.of(response.statusCode());
            }
            RequestResponse processedResponse = convertResponse(response);
            list.addAll(processedResponse.resultJson());
            if (!nextPage.isEmpty()) {
                request = requestCreator.makeRequest(nextPage);
            }
        } while (!nextPage.isEmpty());
        return new RequestResponse(statusCode.get(), list);
    }
}
