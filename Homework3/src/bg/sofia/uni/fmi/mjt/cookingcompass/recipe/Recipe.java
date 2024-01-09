package bg.sofia.uni.fmi.mjt.cookingcompass.recipe;

import java.util.List;

public class Recipe {

    String label;
    List<String> dietLabels;
    List<String> healthLabels;
    int totalWeight;
    List<String> cuisineType;
    List<String> mealType;
    List<String> dishType;
    List<String> ingredientLines;

    public Recipe(String label, List<String> dietLabels, List<String> healthLabels, int totalWeight,
                  List<String> cuisineType, List<String> mealType, List<String> dishType,
                  List<String> ingredientLines) {
        this.label = label;
        this.dietLabels = dietLabels;
        this.healthLabels = healthLabels;
        this.totalWeight = totalWeight;
        this.cuisineType = cuisineType;
        this.mealType = mealType;
        this.dishType = dishType;
        this.ingredientLines = ingredientLines;
    }

    public String getRecipe() {
        return String.join("\n", ingredientLines).replaceAll("\n(?!\\*)", "");
    }
}
