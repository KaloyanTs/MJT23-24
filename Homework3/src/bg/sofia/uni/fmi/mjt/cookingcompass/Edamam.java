package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.client.EdamamClient;
import bg.sofia.uni.fmi.mjt.cookingcompass.converter.ConverterFromJson;
import bg.sofia.uni.fmi.mjt.cookingcompass.unit.EdamamRecipe;

import java.util.List;

public class Edamam {

    private final EdamamClient client;

    public Edamam(EdamamClient client) {
        this.client = client;
    }

    public List<EdamamRecipe> getRecipesByKeywords(String... keywords) {
        return ConverterFromJson.getInstance().convertResponse(client.makeRequest(keywords), EdamamRecipe.class);
    }
}
