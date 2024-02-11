package bg.sofia.uni.fmi.mjt.passvault.server;

import bg.sofia.uni.fmi.mjt.passvault.command.interpreter.CommandInterpreter;
import bg.sofia.uni.fmi.mjt.passvault.client.Request;
import bg.sofia.uni.fmi.mjt.passvault.password.saver.FilePasswordSaver;
import bg.sofia.uni.fmi.mjt.passvault.password.checker.WebPasswordChecker;
import bg.sofia.uni.fmi.mjt.passvault.utility.Response;
import bg.sofia.uni.fmi.mjt.passvault.vault.Vault;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static final int SERVER_PORT = 6154;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 4096;
    private static final CommandInterpreter INTERPRETER;

    static {
        INTERPRETER = new CommandInterpreter(new Vault(new WebPasswordChecker(), new FilePasswordSaver()));
    }

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();

                        buffer.clear();
                        int r = sc.read(buffer);
                        if (r < 0) {
                            System.out.println("Client has closed the connection...");
                            sc.close();
                            continue;
                        }

                        buffer.flip();

                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));

                        Request clientRequest = (Request) ois.readObject();
                        Response response = INTERPRETER.intepretate(clientRequest);
                        if (response == null) {
                            System.out.println("Client desires to close the connection...");
                            sc.close();
                            continue;
                        }

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
                        outputStream.writeObject(response);
                        outputStream.flush();
                        byte[] responseData = byteArrayOutputStream.toByteArray();
                        buffer.clear();
                        buffer.put(responseData);
                        buffer.flip();
                        sc.write(buffer);

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                    }

                    keyIterator.remove();
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("There is a problem with the server", e);
        }
    }
}