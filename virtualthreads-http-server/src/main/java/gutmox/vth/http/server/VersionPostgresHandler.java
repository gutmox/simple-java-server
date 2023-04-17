package gutmox.vth.http.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.*;

import java.io.IOException;

public class VersionPostgresHandler implements HttpHandler {

    private final SqlClient client;

    public VersionPostgresHandler() {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("mydb")
                .setUser("user")
                .setPassword("topsecret");
        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        client = PgPool.client(connectOptions, poolOptions);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        client.query("SELECT version()")
                .execute()
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> result = ar.result();
                        System.out.println("Got " + result.size() + " rows ");
                        String version = result.iterator().next().getString("version");
                        buildResponse(exchange, version, 200);
                    } else {
                        ar.cause().printStackTrace();
                        buildResponse(exchange, "Error", 500);
                    }
                    client.close();
                });
    }

    private static void buildResponse(HttpExchange exchange, String response, int code) {
        try {
            exchange.sendResponseHeaders(code, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
