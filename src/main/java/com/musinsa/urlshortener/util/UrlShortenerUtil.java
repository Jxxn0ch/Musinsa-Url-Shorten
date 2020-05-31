package com.musinsa.urlshortener.util;

import java.util.Random;

public class UrlShortenerUtil {

    static final String BASE62String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";
    private static final Random random = new Random();

    /**
     * 입력된 주소의 HTTP / HTTPS 제거
     *
     * @param url
     * @return String
     */
    public static String removeHttp(String url) {
        if (url.contains(HTTP_PREFIX)) {
                url = url.substring(7);
            } else if (url.contains(HTTPS_PREFIX)) {
                url = url.substring(8);
            }

        if (url.charAt(url.length() - 1) == '/') {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }

    /**
     * 8자리 축소 uri 대용 문자 생성
     *
     * @return String
     */
    public static String generateUrl() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i <= 7; i++) {
            sb.append(BASE62String.charAt(random.nextInt(62)));
        }

        return sb.toString();
    }
}
