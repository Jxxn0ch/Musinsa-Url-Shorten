package com.musinsa.urlshortener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.urlshortener.dto.request.UrlShortenerRequestDto;
import com.musinsa.urlshortener.dto.response.UrlShortenerResponseDto;
import com.musinsa.urlshortener.entity.UrlShortenerEntity;
import com.musinsa.urlshortener.exception.ShortenUrlNotFoundException;
import com.musinsa.urlshortener.repository.UrlShortenerRepository;
import com.musinsa.urlshortener.util.UrlShortenerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UrlShortenerServiceImpl implements UrlShortenerService {

    @Autowired
    UrlShortenerRepository urlShortenRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${app.host}")
    String host;

    /**
     * URL 축소 서비스
     *
     * @param urlShortenRequestDto
     * @return UrlShortenerResponseDto
     */
    @Override
    public UrlShortenerResponseDto requestShortenUrl(UrlShortenerRequestDto urlShortenRequestDto) {
        String originUrl =  UrlShortenerUtil.removeHttp(urlShortenRequestDto.getUrl());

        // 값 유뮤 판별 후 없으면 생성, 있으면 요청 카운트 증가
        return urlShortenRepository.findByOriginUrl(originUrl)
                .map(this::updateShortenUrlRequestCount)
                .orElseGet(() -> createShortenUrl(originUrl));
    }

    /**
     * 축소 URL 호출 시 리다이렉트
     *
     * @param shortenUrl
     * @return String
     * @throws ShortenUrlNotFoundException
     */
    @Override
    public String redirectShortenUrl(String shortenUrl) throws ShortenUrlNotFoundException {
        UrlShortenerEntity urlShortenEntity = urlShortenRepository.findByShortUrl(shortenUrl)
                .orElseThrow(() -> new ShortenUrlNotFoundException(shortenUrl));
        return urlShortenEntity.getOriginUrl();
    }

    /**
     * URL 요청 카운트 증가
     *
     * @param urlShortenEntity
     * @return UrlShortenerResponseDto
     */
    private UrlShortenerResponseDto updateShortenUrlRequestCount(UrlShortenerEntity urlShortenEntity) {
        urlShortenEntity.setRequestCount(urlShortenEntity.getRequestCount() + 1);
        return UrlShortenerResponseDto.of(urlShortenEntity, host);
    }

    /**
     * 신규 URL 생성
    *
     * @param originUrl
     * @return UrlShortenerResponseDto
     */
    private UrlShortenerResponseDto createShortenUrl(String originUrl) {
        UrlShortenerEntity urlShortenEntity = urlShortenRepository.save(UrlShortenerEntity.builder()
                .originUrl(originUrl)
                .shortUrl(generateShortUrl())
                .requestCount(1)
                .build());

        return UrlShortenerResponseDto.of(urlShortenEntity, host);
    }

    /**
     * URL Key 발행
     *
     * @return String
     */
    private String generateShortUrl() {
        while(true) {
            String tempShortUrl = UrlShortenerUtil.generateUrl();
            if (validationShortUrl(tempShortUrl)) {
                return tempShortUrl;
            }
        }
    }

    /**
     * 이미 발행된 축소 URL인지 확인
     *
     * @param shortUrl
     * @return
     */
    private boolean validationShortUrl(String shortUrl) {
        return urlShortenRepository.findByShortUrl(shortUrl).isEmpty();
    }
}