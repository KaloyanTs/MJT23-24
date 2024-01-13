package bg.sofia.uni.fmi.mjt.cookingcompass.page;

import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawRequestResponse;

public interface PageMover {

    String getNextPage(RawRequestResponse element);
}
