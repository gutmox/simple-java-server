package gutmox.vth.http.server.async;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AsyncNioFileReader {

    public static void main(String[] args) {
        var future = readFileAsync("random.json");
        future.thenAccept(System.out::println);
        future.join();
    }

    private static void readFile(String filename) {
        var path = Paths.get(Objects.requireNonNull(AsyncNioFileReader.class.getClassLoader().getResource(filename)).getPath());
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(path)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Future<Integer> result = channel.read(buffer, 0);
            while (!result.isDone()) {
                System.out.println("Reading file...");
            }
            int bytesRead = result.get();
            buffer.flip();
            String fileContents = StandardCharsets.UTF_8.decode(buffer).toString();
            System.out.println(fileContents);
        } catch (IOException | InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    public static CompletableFuture<String> readFileAsync(String fileName) {
        Path filePath = Paths.get(Objects.requireNonNull(AsyncNioFileReader.class.getClassLoader().getResource("random.json")).getPath());
        CompletableFuture<String> future = new CompletableFuture<>();
        AsynchronousFileChannel fileChannel;
        try {
            fileChannel = AsynchronousFileChannel.open(filePath, StandardOpenOption.READ);
        } catch (IOException ex) {
            future.completeExceptionally(ex);
            return future;
        }

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        fileChannel.read(buffer, 0, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer result, Void attachment) {
                buffer.flip();
                String fileContents = StandardCharsets.UTF_8.decode(buffer).toString();
                future.complete(fileContents);
                closeChannel(fileChannel);
            }

            @Override
            public void failed(Throwable ex, Void attachment) {
                future.completeExceptionally(ex);
                closeChannel(fileChannel);
            }
        });

        return future;
    }

    private static void closeChannel(AsynchronousFileChannel fileChannel) {
        try {
            fileChannel.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
