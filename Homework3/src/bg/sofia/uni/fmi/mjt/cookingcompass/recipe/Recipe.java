package bg.sofia.uni.fmi.mjt.cookingcompass.recipe;

import java.util.List;

public record Recipe(String label,
                     List<String> dietLabels,
                     List<String> healthLabels,
                     int totalWeight,
                     List<String> cuisineType,
                     List<String> mealType,
                     List<String> dishType,
                     List<String> ingredientLines) {
}
