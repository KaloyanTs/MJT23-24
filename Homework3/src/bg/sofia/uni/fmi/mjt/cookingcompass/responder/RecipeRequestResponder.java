package bg.sofia.uni.fmi.mjt.cookingcompass.responder;

import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public interface RecipeRequestResponder {

    /**
     * @param request request to be responded
     * @return RecipeRequestResponse object, containing statusCode of the request and body as string
     **/
    RequestResponse proceedRequest(String request) throws URISyntaxException, MalformedURLException;
    //todo replace with new class Request and return list of recipes
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
}
