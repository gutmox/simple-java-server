package gutmox.vth.http.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException {
        runServer();
    }

    private static void runServer() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/health", new SimpleHandler());
        httpServer.createContext("/file", new FIleNioReaderHandler());
        httpServer.createContext("/postgres", new VersionPostgresHandler());
        httpServer.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        httpServer.start();
    }
}