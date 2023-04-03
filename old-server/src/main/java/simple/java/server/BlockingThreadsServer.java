package simple.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingThreadsServer {

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                final Socket socket = serverSocket.accept(); // Blocking until someone connects
                new Thread(() -> handle(socket)).start();
            }
        }
    }

    private static void handle(final Socket socket) {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            int data;
            while ((data = inputStream.read()) != -1) { // -1 -> end of stream
                // Data read from socket

                // Operation performed
                data = Character.isLetter(data) ? invertCase(data) : data;

                // Changed output written back to socket
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
