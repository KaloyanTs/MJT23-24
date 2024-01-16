package bg.sofia.uni.fmi.mjt.cookingcompass.page;

import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawResponse;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdamamPageMoverTest {

    @Test
    void testGetNextPageHas() {
        EdamamPageMover pageMover = new EdamamPageMover(new GsonBuilder().setPrettyPrinting().create());

        RawResponse response = new RawResponse(0, """
            {
                "_links":{
                    "next":{
                        "href":"next_page_link",
                        "title": "Next page"
                    }
                }
            }""");

        assertEquals("next_page_link", pageMover.getNextPage(response));
    }

    @Test
    void testGetNextPageNoNext() {
        EdamamPageMover pageMover = new EdamamPageMover(new GsonBuilder().setPrettyPrinting().create());

        RawResponse response = new RawResponse(0, """
            {
                "links":"link",
                "field": {
                    "a": 1,
                    "b": 2
                }
            }""");

        assertEquals("", pageMover.getNextPage(response));
    }
}
