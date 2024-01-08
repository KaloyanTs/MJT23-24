package bg.sofia.uni.fmi.mjt.cookingcompass;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface RecipeRequestResponder {

    /**
     * @param request request to be responded
     * @return RecipeRequestResponse object, containing statusCode of the request and body as string
     **/
    RecipeRequestResponse proceedRequest(String request) throws URISyntaxException, MalformedURLException;
}
