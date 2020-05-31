package com.musinsa.urlshortener.util;

import java.util.Random;

public class UrlShortenUtil {

    static final char[] BASE62Char = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "http://";

    public static String removeHttp(String url) {
        if(url.contains(HTTP_PREFIX) || url.contains(HTTPS_PREFIX)) {
            if (url.substring(0, 7).contains(HTTP_PREFIX))
                url = url.substring(7);
            else if (url.substring(0, 8).contains(HTTPS_PREFIX))
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
