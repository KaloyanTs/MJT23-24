package bg.sofia.uni.fmi.mjt.passvault.client;

import bg.sofia.uni.fmi.mjt.passvault.utility.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
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

            System.out.println("Connected to the vault. (for manual type m)");

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine(); // read a line from the console

                if (message.equals("m")) {
                    System.out.println("""
                        register <user> <password> <password-repeat>
                        login <user> <password>
                        logout
                        retrieve-credentials <website> <user>
                        generate-password <website> <user>
                        add-password <website> <user> <password>
                        remove-password <website> <user>
                        disconnect""");
                    continue;
                }

                BUFFER.clear(); // switch to writing mode
                BUFFER.put(message.getBytes()); // BUFFER fill
                BUFFER.flip(); // switch to reading mode
                socketChannel.write(BUFFER); // BUFFER drain
                //todo remove comments

                BUFFER.clear();
                socketChannel.read(BUFFER);
                //todo debug here
                BUFFER.flip();

                byte[] data = new byte[BUFFER.remaining()];
                BUFFER.get(data);
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);

                Response responseFromServer = (Response) ois.readObject();
                System.out.println(responseFromServer.content());
                if (responseFromServer.password() != null) {
                    System.out.println(responseFromServer.password().getDecrypted());
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}