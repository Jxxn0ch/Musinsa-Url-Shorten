package com.musinsa.urlshortener.dto.response;

import com.musinsa.urlshortener.entity.UrlShortenerEntity;
import lombok.*;

@Getter
@Setter
@Builder
public class UrlShortenerResponseDto {
    private int requestCount;
    private String originUrl;
    private String shortenUrl;

    public static UrlShortenerResponseDto of(UrlShortenerEntity urlShortenEntity, String host) {
        return UrlShortenerResponseDto.builder()
                .originUrl(urlShortenEntity.getOriginUrl())
                .shortenUrl(host + urlShortenEntity.getShortUrl())
                .requestCount(urlShortenEntity.getRequestCount())
                .build();
    }
}