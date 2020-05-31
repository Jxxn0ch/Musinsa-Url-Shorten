package com.musinsa.urlshortener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.urlshortener.dto.request.UrlShortenRequestDto;
import com.musinsa.urlshortener.dto.response.ResponseDto;
import com.musinsa.urlshortener.entity.UrlShortenEntity;
import com.musinsa.urlshortener.exception.ShortenUrlNotFoundException;
import com.musinsa.urlshortener.repository.UrlShortenRepository;
import com.musinsa.urlshortener.util.UrlShortenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UrlShortenServiceImpl implements UrlShortenService {

    @Autowired
    UrlShortenRepository urlShortenRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${app.host}")
    String host;

    @Override
    public ResponseDto requestShortenUrl(UrlShortenRequestDto urlShortenRequestDto) {
        String originUrl =  UrlShortenUtil.removeHttp(urlShortenRequestDto.getUrl());

        return urlShortenRepository.findByOriginUrl(originUrl)
                .map(u -> {
                    u.setRequestCount(u.getRequestCount() + 1);
                    return ResponseDto.builder()
                            .originUrl(u.getOriginUrl())
                            .shortenUrl(host + u.getShortUrl())
                            .requestCount(u.getRequestCount())
                            .build();
                })
                .orElseGet(() -> createShortenUrl(originUrl));
    }

    @Override
    public String redirectShortenUrl(String shortenUrl) throws ShortenUrlNotFoundException {
        UrlShortenEntity urlShortenEntity = urlShortenRepository.findByShortUrl(shortenUrl)
                .orElseThrow(() -> new ShortenUrlNotFoundException(shortenUrl));
        return urlShortenEntity.getOriginUrl();
    }

    private ResponseDto createShortenUrl(String originUrl) {
        UrlShortenEntity urlShortenEntity = urlShortenRepository.save(UrlShortenEntity.builder()
                .originUrl(originUrl)
                .shortUrl(generateShortUrl())
                .requestCount(1)
                .build());

        return ResponseDto.builder()
                .originUrl(urlShortenEntity.getOriginUrl())
                .shortenUrl(host + urlShortenEntity.getShortUrl())
                .requestCount(urlShortenEntity.getRequestCount())
                .build();
    }

    private String generateShortUrl() {
        while(true) {
            String tempShortUrl = UrlShortenUtil.generateUrl();
            if (validationShortUrl(tempShortUrl)) {
                return tempShortUrl;
            }
        }
    }

    private boolean validationShortUrl(String shortUrl) {
        return urlShortenRepository.findByShortUrl(shortUrl).isEmpty();
    }
}
