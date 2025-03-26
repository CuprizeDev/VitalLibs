package com.vitaldev.vitallibs.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AsyncUtil {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(8);

    public static <T> CompletableFuture<T> runAsync(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, EXECUTOR);
    }

    public static CompletableFuture<Void> runAsync(Runnable task) {
        return CompletableFuture.runAsync(task, EXECUTOR);
    }

    public static <T, R> CompletableFuture<R> runAsyncThenApply(Supplier<T> task, Function<T, R> transform) {
        return runAsync(task).thenApplyAsync(transform, EXECUTOR);
    }

    public static <T> CompletableFuture<Void> runAsyncThenAccept(Supplier<T> task, Consumer<T> consumer) {
        return runAsync(task).thenAcceptAsync(consumer, EXECUTOR);
    }

    public static <T> CompletableFuture<List<T>> runAllAsync(List<Supplier<T>> tasks) {
        List<CompletableFuture<T>> futures = tasks.stream()
                .map(AsyncUtil::runAsync)
                .toList();
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).toList());
    }

    public static <T> CompletableFuture<List<T>> combineFutures(List<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).toList());
    }

    public static CompletableFuture<Void> delay(long milliseconds) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, EXECUTOR);
    }

    public static void shutdown() {
        EXECUTOR.shutdown();
    }
}
