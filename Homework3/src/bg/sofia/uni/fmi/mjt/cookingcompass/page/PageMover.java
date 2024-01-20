package bg.sofia.uni.fmi.mjt.cookingcompass.page;

import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawResponse;

public interface PageMover {
    String getNextPage(RawResponse element);
}
