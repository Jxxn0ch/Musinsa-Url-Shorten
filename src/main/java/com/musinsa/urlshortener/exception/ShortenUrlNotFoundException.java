package com.musinsa.urlshortener.exception;

public class ShortenUrlNotFoundException extends RuntimeException{
    public ShortenUrlNotFoundException(String url) {
        super(String.format("잘못된 주소입니다. [주소 : %s]", url));
    }
}