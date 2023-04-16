package simple.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIONonBlockingHttpServer {

    public static void main(String[] args) throws IOException {
        var socket = ServerSocketChannel.open();
        socket.configureBlocking(false);
        socket.socket().bind(new InetSocketAddress(8080));
        while(true){
            SocketChannel socketChannel = socket.accept();
            if(socketChannel != null){
                handle(socketChannel);
            }
        }
    }

    private static void handle(final SocketChannel socket) throws IOException {
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(80);
        while (socket.read(byteBuffer) != -1) {
            byteBuffer.flip();
            invertCase(byteBuffer);
            socket.write(byteBuffer.rewind());
            byteBuffer.compact();
        }
    }

    private static void invertCase(final ByteBuffer byteBuffer) {
        for (int x = 0; x < byteBuffer.limit(); x++) {
            byteBuffer.put(x, (byte) invertCase(byteBuffer.get(x)));
        }
    }
    private static int invertCase(final int data) {
        return Character.isUpperCase(data)
                ? Character.toLowerCase(data)
                : Character.toUpperCase(data);
    }
}
