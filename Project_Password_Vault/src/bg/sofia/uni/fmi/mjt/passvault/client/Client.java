package bg.sofia.uni.fmi.mjt.passvault.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// NIO, blocking
public class Client {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 512;

    private static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine(); // read a line from the console

                if ("quit".equals(message)) {
                    break;
                }

                System.out.println("Sending message <" + message + "> to the server...");

                BUFFER.clear(); // switch to writing mode
                BUFFER.put(message.getBytes()); // BUFFER fill
                BUFFER.flip(); // switch to reading mode
                socketChannel.write(BUFFER); // BUFFER drain
                //todo remove comments
                BUFFER.clear(); // switch to writing mode
                socketChannel.read(BUFFER); // BUFFER fill
                BUFFER.flip(); // switch to reading mode

                byte[] byteArray = new byte[BUFFER.remaining()];
                BUFFER.get(byteArray);
                String reply = new String(byteArray, StandardCharsets.UTF_8);

                System.out.println("The server replied <" + reply + ">");
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}