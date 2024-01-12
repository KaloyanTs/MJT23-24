package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.client.EdamamClient;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

public class Main {

    public static void main(String[] args) throws SocketException {
        EdamamClient client = new EdamamClient("4abc3395", "b5cd0fc2f0bd828ebc6f6796cd5fe5e1");

        RequestResponse veggieChickenSoupsJson =
            client.makeRequest("chicken", "vegetarian", "soup", "tomato");

        System.out.println(veggieChickenSoupsJson.statusCode());

        Collections.list(NetworkInterface.getNetworkInterfaces())
            .stream()
            .forEach(n -> {
                System.out.println("Disp. name: " + n.getDisplayName());
                System.out.println("Name: " + n.getName());
            });
    }
}
