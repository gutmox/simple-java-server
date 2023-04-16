package gutmox.vth.http.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class SimpleHandler implements HttpHandler {
    public SimpleHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var response = "Hello World";
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }
}
