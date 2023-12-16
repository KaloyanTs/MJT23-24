package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.order.Order;

import java.util.Collection;

public record Response(Status status, String additionalInfo, Collection<Order> orders) {
    private enum Status {
        OK, CREATED, DECLINED, NOT_FOUND
    }

    public Response(String status, String additionalInfo, Collection<Order> orders) {
        this(Status.valueOf(status), additionalInfo, orders);
    }
}