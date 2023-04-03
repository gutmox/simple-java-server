package simple.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolBlockingServer {

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            final ExecutorService executorService = Executors.newFixedThreadPool(100);
            while (true) {
                final Socket socket = serverSocket.accept();

                executorService.submit(() -> handle(socket));
            }
        }
    }

    /**
     * Read from socket and do operation and then write back on socket.
     */
    private static void handle(final Socket socket) {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            int data;
            while ((data = inputStream.read()) != -1) {
                data = Character.isLetter(data) ? invertCase(data) : data;
                outputStream.write(data);
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static int invertCase(final int data) {
        return Character.isUpperCase(data)
                ? Character.toLowerCase(data)
                : Character.toUpperCase(data);
    }
}
