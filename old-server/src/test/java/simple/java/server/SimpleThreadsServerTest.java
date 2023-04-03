package simple.java.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

public class SimpleThreadsServerTest {

    @Test
    void checkOpenConnections() throws InterruptedException {
        final Socket[] sockets = new Socket[3000];
        for (int x = 0; x < sockets.length; x++) {
            try {
                sockets[x] = new Socket("localhost", 8080);
            } catch (final IOException e) {
                System.err.println(e);
            }
        }
        Thread.sleep(Long.MAX_VALUE);
    }
}
