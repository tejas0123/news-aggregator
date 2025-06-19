package com.itt.newsaggregatorclient.util;

import java.util.Scanner;
import java.util.function.Supplier;

public class SingletonScanner {
    private static Scanner inputScanner = null;

    private SingletonScanner() {

    }

    public static Supplier<Scanner> scannerSupplier = () -> {
        if(inputScanner == null){
            inputScanner = new Scanner(System.in);
        }
        return inputScanner;
    };
}