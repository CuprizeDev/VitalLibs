package com.vitaldev.vitallibs.util;

public class MathUtil {
    public static double average(int... values) {
        if (values.length == 0) return 0;
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        return (double) sum / values.length;
    }

    public static double average(double... values) {
        if (values.length == 0) return 0;
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    public static double round(double value, int places) {
        long factor = (long) Math.pow(10, places);
        return (double) Math.round(value * factor) / factor;
    }

    public static int factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Value must be non-negative");
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static boolean isPrime(int number) {
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    public static double sqrt(double value) {
        return Math.sqrt(value);
    }

    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    public static double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    public static double randomInRange(double min, double max) {
        return min + (max - min) * Math.random();
    }

    public static double toDegrees(double radians) {
        return Math.toDegrees(radians);
    }

    public static double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    public static int sum(int... values) {
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        return sum;
    }

    public static double sum(double... values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum;
    }
}
