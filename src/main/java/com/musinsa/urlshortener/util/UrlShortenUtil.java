package com.musinsa.urlshortener.util;

import org.springframework.util.StringUtils;

import java.util.Random;

public class UrlShortenUtil {

    static final String BASE62String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";
    private static final Random random = new Random();

    public static String removeHttp(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new NullPointerException("생성하려는 URL 값이 비었습니다.");
        } else {
            if (url.contains(HTTP_PREFIX)) {
                url = url.substring(7);
            } else if (url.contains(HTTPS_PREFIX)) {
                url = url.substring(8);
            }

            if (url.charAt(url.length() - 1) == '/') {
                url = url.substring(0, url.length() - 1);
            }
        }

        return url;
    }

    public static String generateUrl() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i <= 7; i++) {
            sb.append(BASE62String.charAt(random.nextInt(62)));
        }

        return sb.toString();
    }
}
