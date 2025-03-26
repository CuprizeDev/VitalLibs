package com.vitaldev.vitallibs.util;

public class EfficiencyUtil {
    public static void measureExecutionTime(Runnable method) {
        long startTime = System.nanoTime();
        method.run();
        long endTime = System.nanoTime();
        ConsoleUtil.sendMessage(" | Execution time: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }

    public static double measureAverageExecutionTime(Runnable method, int iterations) {
        long totalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            method.run();
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }
        return (totalTime / (iterations * 1_000_000.0));
    }

    public static void measureMemoryUsage(Runnable method) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
        method.run();
        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        ConsoleUtil.sendMessage(" | Memory used: " + (afterMemory - beforeMemory) / 1024 + " KB");
    }

    public static void compareMethods(Runnable method1, Runnable method2, int iterations) {
        double time1 = measureAverageExecutionTime(method1, iterations);
        double time2 = measureAverageExecutionTime(method2, iterations);
        ConsoleUtil.sendMessage(" | Method 1 Time: " + time1 + " ms");
        ConsoleUtil.sendMessage(" | Method 2 Time: " + time2 + " ms");
        ConsoleUtil.sendMessage(" | Difference: " + Math.abs(time1 - time2) + " ms");
    }

    public static int fibRecursive(int n) {
        if (n <= 1) return n;
        return fibRecursive(n - 1) + fibRecursive(n - 2);
    }

    public static int fibIterative(int n) {
        if (n <= 1) return n;
        int a = 0, b = 1, sum;
        for (int i = 2; i <= n; i++) {
            sum = a + b;
            a = b;
            b = sum;
        }
        return b;
    }
}
