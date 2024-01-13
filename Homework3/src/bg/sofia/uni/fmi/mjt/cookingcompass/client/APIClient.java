package bg.sofia.uni.fmi.mjt.cookingcompass.client;

import bg.sofia.uni.fmi.mjt.cookingcompass.exception.BadCredentialsException;
import bg.sofia.uni.fmi.mjt.cookingcompass.exception.NotAuthorizedException;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.RequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.retriever.DataRetriever;

import java.util.Arrays;

public abstract class APIClient {
    //todo must receive other abstractions and use them together independent of the implementation

    private DataRetriever dataRetriever;
    private RequestCreator requestCreator;
    private boolean hasCredentials;

    public RequestResponse makeRequest(String... keywords) {
        if (!hasCredentials) {
            throw new NotAuthorizedException("Credentials not given yet...");
        }
        Arrays.sort(keywords);
        return dataRetriever.retrieveAllData(keywords);
    }

    protected APIClient() {
        this.hasCredentials = false;
    }

    /**
     * @param credentials array of needed credentials for authorization in the API
     * @throws BadCredentialsException given credentials are not in desired format
     */
    abstract void setCredentials(String... credentials) throws BadCredentialsException;

    public APIClient user(String... credentials) throws BadCredentialsException {
        setCredentials(credentials);
        this.hasCredentials = true;
        return this;
    }

    public boolean doesHaveCredentials() {
        return hasCredentials;
    }
}
