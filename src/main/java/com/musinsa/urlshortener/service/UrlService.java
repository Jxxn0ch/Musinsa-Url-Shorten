package com.musinsa.urlshortener.service;

import com.musinsa.urlshortener.dto.request.UrlShortenRequestDto;
import com.musinsa.urlshortener.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface UrlService {
    ResponseEntity<ResponseDto> requestShortenUrl(UrlShortenRequestDto urlShortenRequestDto);

    String redirectShortenUrl(String shortenUrl, HttpServletResponse response);
}
