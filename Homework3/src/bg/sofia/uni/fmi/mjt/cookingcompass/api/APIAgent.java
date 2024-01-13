package bg.sofia.uni.fmi.mjt.cookingcompass.api;

import bg.sofia.uni.fmi.mjt.cookingcompass.converter.ConverterFromJson;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.retriever.DataRetriever;

import java.util.List;

public class APIAgent {

    private final DataRetriever dataRetriever;

    public APIAgent(DataRetriever dataRetriever) {
        this.dataRetriever = dataRetriever;
    }

    public <T> List<T> byKeywords(Class<T> tClass, String... keywords) {
        RequestResponse response = dataRetriever.retrieveAllData(keywords);
        return ConverterFromJson.convertResponse(response, tClass);
    }
}
