package com.musinsa.urlshortener.service;

import com.musinsa.urlshortener.dto.request.UrlShortenRequestDto;
import com.musinsa.urlshortener.dto.response.ResponseDto;
import com.musinsa.urlshortener.exception.ShortenUrlNotFoundException;

public interface UrlShortenService {
    ResponseDto requestShortenUrl(UrlShortenRequestDto urlShortenRequestDto);
    String redirectShortenUrl(String shortenUrl) throws ShortenUrlNotFoundException;
}