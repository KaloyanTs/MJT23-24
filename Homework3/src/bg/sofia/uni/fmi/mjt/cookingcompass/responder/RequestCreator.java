package bg.sofia.uni.fmi.mjt.cookingcompass.responder;

import java.util.List;

public interface RequestCreator {

    String makeRequest(List<String> keywords);
}
