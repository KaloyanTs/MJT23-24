package bg.sofia.uni.fmi.mjt.passvault.client;

import bg.sofia.uni.fmi.mjt.passvault.utility.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {

    private static final int SERVER_PORT = 6154;
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
                        remove-password <website>
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
                BUFFER.flip();

                byte[] data = new byte[BUFFER.remaining()];
                BUFFER.get(data);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));

                Response responseFromServer = (Response) ois.readObject();
                System.out.println(responseFromServer.content());
                if (responseFromServer.user() != null) {
                    System.out.println(responseFromServer.user().name());
                }
                if (responseFromServer.password() != null) {
                    System.out.println(responseFromServer.password().getDecrypted());
                    //------------------------------
                    //todo debug (problem with deciphering different passwords
                    //------------------------------
                    //todo implement cookies (vault is collection  of map<user, Map<Website, Pair<User, Password>>>)
                    //todo client keeps his credentials after logging in and sends them back to server every time
                    //todo modify Response for cookies
                    //todo writing and reading from files on load...
                    //todo implement UserContainer and use it in vault (map<User, UserContainer>)...
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}