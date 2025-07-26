package com.itt.newsaggregatorclient;

import com.itt.newsaggregatorclient.io.UserAuthIO;

public class Main {
    public static void main(String[] args) {
        UserAuthIO userAuthIO = new UserAuthIO();
        userAuthIO.startApplication();
    }
}