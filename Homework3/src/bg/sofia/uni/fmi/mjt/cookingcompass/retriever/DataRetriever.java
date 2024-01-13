package bg.sofia.uni.fmi.mjt.cookingcompass.retriever;

import bg.sofia.uni.fmi.mjt.cookingcompass.request.Request;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.RequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawRequestResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;

public abstract class DataRetriever {

    protected RequestCreator requestCreator;

    DataRetriever(RequestCreator requestCreator) {
        this.requestCreator = requestCreator;
    }

    /**
     * @param request Request containing all needed to retrieve data from a dataset
     * @return RequestResponse containing status of the request and retrieved data in JSON
     */
    protected abstract RawRequestResponse retrieveData(Request request);

    public RequestResponse retrieveAllData(String... keywords) {
        return convertResponse(retrieveData(requestCreator.makeRequest(keywords)));
    }

    protected abstract RequestResponse convertResponse(RawRequestResponse response);
}