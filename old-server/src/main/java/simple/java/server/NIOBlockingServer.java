package simple.java.server;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOBlockingServer {

    public static void main(String[] args) throws IOException {
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(8080));
            while (true) {
                final SocketChannel socket = serverSocket.accept();
                handle(socket);
            }
        }
    }

    /**
     * Read from socket and do operation and then write back on socket.
     *
     * @param socket
     */
    private static void handle(final SocketChannel socket) {
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(80); // Allocate to Native memory
        try {
            int data;
            while ((data = socket.read(byteBuffer)) != -1) { // -1 -> end of stream
                // Data read from socket
                byteBuffer.flip();
                // Operation performed
                invertCase(byteBuffer);
                // Changed output written back to socket
                while (byteBuffer.hasRemaining()) {
                    // OutputStream write everything together, but ByteBuffer doesn't not, so this loop is required
                    socket.write(byteBuffer);
                }

                // It will set the position=0 and limit=80
                byteBuffer.compact();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void invertCase(final ByteBuffer byteBuffer) {
        for (int x = 0; x < byteBuffer.limit(); x++) { // read every byte in it.
            byteBuffer.put(x, (byte) invertCase(byteBuffer.get(x)));
        }
    }

    private static int invertCase(final int data) {
        return Character.isLetter(data) ?

                Character.isUpperCase(data)
                        ? Character.toLowerCase(data)
                        : Character.toUpperCase(data) :

                data;
    }
}
