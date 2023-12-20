package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MJTOrderRepository implements OrderRepository {

    private final List<Order> invalidOrders;
    private final Map<Integer, Order> orders;
    private int idCounter;

    public MJTOrderRepository() {
        invalidOrders = new ArrayList<>();
        orders = new HashMap<>();
        idCounter = 1;
    }

    @Override
    public Response request(String sizeStr, String colorStr, String destinationStr) {
        if (sizeStr == null || colorStr == null || destinationStr == null) {
            throw new IllegalArgumentException("Null given as argument...");
        }
        String badArguments = "";
        Size size = Size.fromString(sizeStr);
        Color color = Color.fromString(colorStr);
        Destination destination = Destination.fromString(destinationStr);
        if (size == Size.UNKNOWN) {
            badArguments += "size,";
        }
        if (color == Color.UNKNOWN) {
            badArguments += "color,";
        }
        if (destination == Destination.UNKNOWN) {
            badArguments += "destination,";
        }
        if (!badArguments.isEmpty()) {
            invalidOrders.add(new Order(-1, new TShirt(size, color), destination));
            return Response.decline("invalid=" + badArguments.substring(0, badArguments.length() - 1));
        }
        orders.put(idCounter, new Order(idCounter, new TShirt(size, color), destination));
        return Response.create(idCounter++);
    }

    @Override
    public Response getOrderById(int id) {
        if (orders.get(id) == null) {
            return Response.notFound(id);
        }
        return Response.ok(List.of(orders.get(id)));
    }

    @Override
    public Response getAllOrders() {
        List<Order> res = new ArrayList<>();
        res.addAll(orders.values());
        res.addAll(invalidOrders);
        return Response.ok(res);
    }

    @Override
    public Response getAllSuccessfulOrders() {
        return Response.ok(orders.values());
    }
}
