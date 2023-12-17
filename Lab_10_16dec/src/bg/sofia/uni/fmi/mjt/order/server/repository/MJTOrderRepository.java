package bg.sofia.uni.fmi.mjt.order.server.repository;

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
        String badArguments = "";
        Size size;
        Color color;
        Destination destination;
        try {
            size = Size.valueOf(sizeStr);
        } catch (IllegalArgumentException e) {
            size = Size.UNKNOWN;
        }
        try {
            color = Color.valueOf(colorStr);
        } catch (IllegalArgumentException e) {
            color = Color.UNKNOWN;
        }
        try {
            destination = Destination.valueOf(destinationStr);
        } catch (IllegalArgumentException e) {
            destination = Destination.UNKNOWN;
        }
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

            return new Response("DECLINED", "invalid:" + badArguments.substring(0, badArguments.length() - 1), null);
        }

        orders.put(idCounter, new Order(idCounter, new TShirt(size, color), destination));

        return new Response("CREATED", "ORDER_ID=" + idCounter++, null);
    }

    @Override
    public Response getOrderById(int id) {
        if (orders.get(id) == null) {
            return new Response("NOT_FOUND", "Order with id = " + id + " does not exist.", null);
        }
        return new Response("OK", "", List.of(orders.get(id)));
    }

    @Override
    public Response getAllOrders() {
        List<Order> res = new ArrayList<>();
        res.addAll(orders.values());
        res.addAll(invalidOrders);
        return new Response("OK", "", res);
    }

    @Override
    public Response getAllSuccessfulOrders() {
        return new Response("OK", "", orders.values());
    }
}
