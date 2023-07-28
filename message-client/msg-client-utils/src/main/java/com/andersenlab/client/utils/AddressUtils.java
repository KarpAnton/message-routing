package com.andersenlab.client.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddressUtils {

    private AddressUtils() {

    }

    public static String extractCurAddress() {
        try {
            String port = System.getenv("server.port");
            return "http://" +
                    InetAddress.getLocalHost().getHostAddress() +
                    ":" +
                    port;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
