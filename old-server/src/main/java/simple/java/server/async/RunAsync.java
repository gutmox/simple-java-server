package simple.java.server.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RunAsync {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Executing from: " + Thread.currentThread().getName());
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                // delaying the thread by 2 seconds
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            // fetching the name of the Thread
            return "Executing from: " + Thread.currentThread().getName();
            // Output: Executing from: ForkJoinPool.commonPool-worker-3
        });
        // get() blocks and then gets the result of the Future
        String s = completableFuture.get();
        System.out.println(s);
    }
}
