package com.musinsa.urlshortener.exception;

public class NotFoundException extends Exception{
    public NotFoundException(String url) {
        super(String.format("잘못된 주소입니다. [주소 : %s]", url));
    }
}
