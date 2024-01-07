package bg.sofia.uni.fmi.mjt.cookingcompass.recipe;

public record Ingredient(String text,
                         int quantity,
                         String measure,
                         String food,
                         int weight,
                         String foodCategory,
                         String foodId, String image) {
}
