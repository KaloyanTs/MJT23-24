package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;
import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TShirtShopServer {

    private static final int SERVER_PORT = 4444;
    private static final int MAX_EXECUTOR_THREADS = 10;

    public static void main(String[] args) {

        OrderRepository repository = new MJTOrderRepository();

        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

        Thread.currentThread().setName("T-shirt Shop Server Thread");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            Socket clientSocket;

            while (true) {

                clientSocket = serverSocket.accept();

                System.out.println("Accepted connection request from client " + clientSocket.getInetAddress());

                ClientRequestHandler clientHandler = new ClientRequestHandler(repository, clientSocket);

                executor.execute(clientHandler);
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }

}
