package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {

    private static final int GET_ID_PARTS = 3;
    private static final int ID_SKIP = 3;
    private static final int SIZE_SKIP = 5;
    private static final int COLOR_SKIP = 6;
    private static final int DESTINATION_SKIP = 7;
    private static final int REQUEST_PARTS = 4;
    private final Socket socket;
    private final OrderRepository repository;

    public ClientRequestHandler(OrderRepository repository, Socket socket) {
        this.socket = socket;
        this.repository = repository;
    }

    private String handleClientRequest(String request) {

        String[] parts = request.split(" ");
        if (parts.length < 1) {
            return "Unknown command";
        }
        switch (parts[0]) {
            case "get":
                if (parts.length < 2) {
                    return "Unknown command";
                }
                return switch (parts[1]) {
                    case "all" -> repository.getAllOrders().toString();
                    case "all-successful" -> repository.getAllSuccessfulOrders().toString();
                    case "my-order" -> {
                        if (parts.length < GET_ID_PARTS) {
                            yield "Unknown command";
                        }
                        yield repository.getOrderById(Integer.parseInt(parts[2].substring(ID_SKIP))).toString();
                    }
                    default -> "Unknown command";
                };
            case "request":
                if (parts.length < REQUEST_PARTS) {
                    return "Unknown command";
                }
                return repository.request(parts[1].substring(SIZE_SKIP),
                    parts[2].substring(COLOR_SKIP),
                    parts[2 + 1].substring(DESTINATION_SKIP)).toString();
            default:
                return "Unknown command";
        }
    }

    @Override
    public void run() {

        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(handleClientRequest(inputLine));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
