package bg.sofia.uni.fmi.mjt.cookingcompass.retriever;

import bg.sofia.uni.fmi.mjt.cookingcompass.request.Request;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.RequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;

public abstract class DataRetriever {

    protected final int okStatus;
    protected RequestCreator requestCreator;

    DataRetriever(int okStatus, RequestCreator requestCreator) {
        this.okStatus = okStatus;
        this.requestCreator = requestCreator;
    }

    /**
     * @param request Request containing all needed to retrieve data from a dataset
     * @return RequestResponse containing status of the request and retrieved data in JSON
     */
    protected abstract RawResponse retrieveData(Request request);

    public RequestResponse retrieveAllData(String... keywords) {
        return convertResponse(retrieveData(requestCreator.makeRequest(keywords)));
    }

    protected abstract RequestResponse convertResponse(RawResponse response);
}
