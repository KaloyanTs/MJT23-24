package bg.sofia.uni.fmi.mjt.cookingcompass.request;

import bg.sofia.uni.fmi.mjt.cookingcompass.api.WebAPIRepresentative;

public abstract class RequestCreator {

    protected WebAPIRepresentative apiRepresentative;

    RequestCreator(WebAPIRepresentative apiRepresentative) {
        this.apiRepresentative = apiRepresentative;
    }

    public abstract Request makeRequest(String ready);

    public abstract Request makeRequest(String... keywords);
}
