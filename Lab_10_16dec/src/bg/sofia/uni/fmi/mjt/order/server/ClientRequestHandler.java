package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {

    private final Socket socket;
    private final OrderRepository repository;

    public ClientRequestHandler(OrderRepository repository, Socket socket) {
        this.socket = socket;
        this.repository = repository;
    }

    private String handleClientRequest(String request) {

        String[] parts = request.split(" ");
        switch (parts[0]) {
            case "get":
                if (parts.length < 2) {
                    return "Unknown command";
                }
                return switch (parts[1]) {
                    case "all" -> repository.getAllOrders().toString();//todo maybe override toString
                    case "all-successful" -> repository.getAllSuccessfulOrders().toString();
                    case "my-order" -> {
                        if (parts.length < 3) {
                            yield "Unknown command";
                        }
                        yield repository.getOrderById(Integer.parseInt(parts[2].substring(3))).toString();
                    }
                    default -> "Unknown command";
                };
            case "request":
                if (parts.length < 4) {
                    return "Unknown command";
                }
                return repository.request(parts[1].substring(5),
                    parts[2].substring(5),
                    parts[3].substring(5)).toString();
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
