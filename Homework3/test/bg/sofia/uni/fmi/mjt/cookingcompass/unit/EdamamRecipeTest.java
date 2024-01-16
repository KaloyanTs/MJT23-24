package bg.sofia.uni.fmi.mjt.cookingcompass.unit;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdamamRecipeTest {

    @Test
    void testGetRecipeFromRecipe() {
        EdamamRecipe r1 = new EdamamRecipe("some dish", List.of("balanced"), List.of("vegetarian", "alcohol-free"), 100,
            List.of("desert"), List.of("breakfast"), List.of("soup"), List.of("*Throw into the fridge", "*don't move"));

        assertEquals("*Throw into the fridge\n*don't move", r1.getRecipe());
    }
}
