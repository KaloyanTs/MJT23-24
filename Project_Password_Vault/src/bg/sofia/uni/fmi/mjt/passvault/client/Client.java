package bg.sofia.uni.fmi.mjt.passvault.client;

import bg.sofia.uni.fmi.mjt.passvault.user.User;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {

    private static final int SERVER_PORT = 6154;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 512;
    private static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(BUFFER_SIZE);
    private static final String MANUAL = """
        register <user> <password> <password-repeat>
        login <user> <password>
        logout
        retrieve-credentials <website> <user>
        generate-password <website> <user>
        add-password <website> <user> <password>
        remove-password <website>
        disconnect""";

    private static void sendRequest(Request request, SocketChannel socketChannel) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(request);
        outputStream.flush();
        byte[] responseData = byteArrayOutputStream.toByteArray();
        BUFFER.clear();
        BUFFER.put(responseData);
        BUFFER.flip();
        socketChannel.write(BUFFER);
    }

    private static Response readResponse(SocketChannel socketChannel) throws IOException, ClassNotFoundException {
        BUFFER.clear();
        socketChannel.read(BUFFER);
        BUFFER.flip();

        byte[] data = new byte[BUFFER.remaining()];
        BUFFER.get(data);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));

        return (Response) ois.readObject();
    }

    private static void proceedResponse(Response responseFromServer) {
        System.out.println(responseFromServer.content());
        if (responseFromServer.user() != null) {
            System.out.println(responseFromServer.user().name());
        }
        if (responseFromServer.password() != null) {
            System.out.println(responseFromServer.password().getDecrypted());
            //------------------------------
            //todo debug (problem with deciphering different passwords
            //------------------------------
            //todo writing and reading from files on load...
        }
    }

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            System.out.println("Connected to the vault. (for manual type m)");
            User currentUser = null;
            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();
                if (message.equals("m")) {
                    System.out.println(MANUAL);
                    continue;
                }
                Request request = new Request(new Cookie(currentUser), message);
                sendRequest(request, socketChannel);
                Response responseFromServer = readResponse(socketChannel);
                if (message.startsWith("login") && responseFromServer.user() != null) {
                    currentUser = responseFromServer.user();
                }
                proceedResponse(responseFromServer);
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}