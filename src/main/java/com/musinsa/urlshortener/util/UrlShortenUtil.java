package com.musinsa.urlshortener.util;

import java.util.Random;

public class UrlShortenUtil {

    static final char[] BASE62Char = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public static String removeHttp(String url) {
        if(url.contains("http://") || url.contains("https://")) {
            if (url.substring(0, 7).equals("http://"))
                url = url.substring(7);
            else if (url.substring(0, 8).equals("https://"))
                url = url.substring(8);
        }

        return url;
    }

    public static String generateUrl() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 7; i++) {
            sb.append(BASE62Char[new Random().nextInt(62)]);
        }

        return sb.toString();
    }
}
