package com.musinsa.urlshortener.service;

import com.musinsa.urlshortener.dto.request.UrlShortenerRequestDto;
import com.musinsa.urlshortener.dto.response.UrlShortenerResponseDto;
import com.musinsa.urlshortener.exception.ShortenUrlNotFoundException;

public interface UrlShortenerService {
    UrlShortenerResponseDto requestShortenUrl(UrlShortenerRequestDto urlShortenRequestDto);
    String redirectShortenUrl(String shortenUrl) throws ShortenUrlNotFoundException;
}