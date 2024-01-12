package bg.sofia.uni.fmi.mjt.cookingcompass.client;

import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.PageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.WebAPIAgent;
import bg.sofia.uni.fmi.mjt.cookingcompass.exception.NotAuthorizedException;
import bg.sofia.uni.fmi.mjt.cookingcompass.handler.RequestHandler;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;

import java.util.Arrays;

public abstract class APIClient {
    //todo must receive other abstractions and use them together independent of the implementation

    PageMover mover;
    RequestHandler handler;
    WebAPIAgent agent;
    boolean hasCredentials;

    public RequestResponse makeRequest(String... keywords) {
        if (!hasCredentials) {
            throw new NotAuthorizedException("Credentials not given yet...");
        }
        Arrays.sort(keywords);
        return handler.getByKeywords(keywords);
    }

    public APIClient(RequestHandler handler) {
        this.handler = handler;
        this.hasCredentials = false;
    }

    protected APIClient() {
    }

    public abstract APIClient user(String... credentials);
}
