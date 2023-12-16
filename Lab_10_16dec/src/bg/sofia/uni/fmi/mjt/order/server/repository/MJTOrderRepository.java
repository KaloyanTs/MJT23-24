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

    private List<Order> invalidOrders;
    private Map<Integer, Order> orders;
    private int ID_COUNTER;

    public MJTOrderRepository() {
        invalidOrders = new ArrayList<>();
        orders = new HashMap<>();
        ID_COUNTER = 0;
    }

    @Override

    public Response request(String sizeStr, String colorStr, String destinationStr) {
        String badArguments = "";
        Size size = Size.valueOf(sizeStr);
        Color color = Color.valueOf(colorStr);
        Destination destination = Destination.valueOf(destinationStr);
        if (size == Size.UNKNOWN) {
            badArguments += "size";
        }
        if (color == Color.UNKNOWN) {
            badArguments += "color";
        }
        if (destination == Destination.UNKNOWN) {
            badArguments += "destination";
        }
        if (badArguments.length() > 0) {
            return new Response("DECLINED", "invalid:" + badArguments.substring(0, badArguments.length() - 1), null);
        }

        orders.put(ID_COUNTER, new Order(ID_COUNTER, new TShirt(size, color), destination));

        return new Response("CREATED", "ORDER_ID=" + ID_COUNTER++, null);
    }

    @Override
    public Response getOrderById(int id) {
        if (id == -1) {
            //todo
            return null;
        }
        if (orders.get(id) == null) {
            return new Response("NOT_FOUND", "Order with id = " + id + " does not exist.", null);
        }
        return new Response("OK", "", List.of(orders.get(id)));
    }

    @Override
    public Response getAllOrders() {
        List<Order> res = List.copyOf(orders.values());
        res.addAll(invalidOrders);
        return new Response("OK", "", res);
    }

    @Override
    public Response getAllSuccessfulOrders() {
        return new Response("OK", "", orders.values());
    }
}
