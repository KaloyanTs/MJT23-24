package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.client.EdamamClient;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;

public class Main {

    public static void main(String[] args) {
        EdamamClient client = new EdamamClient("4abc3395", "b5cd0fc2f0bd828ebc6f6796cd5fe5e1");

        RequestResponse veggieChickenSoupsJson =
            client.makeRequest("chicken", "vegetarian", "soup", "tomato");

        System.out.println(veggieChickenSoupsJson.statusCode());
    }
}
