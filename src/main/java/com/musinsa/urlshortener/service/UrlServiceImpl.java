package com.musinsa.urlshortener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.urlshortener.dto.request.UrlShortenRequestDto;
import com.musinsa.urlshortener.dto.response.ResponseDto;
import com.musinsa.urlshortener.entity.UrlShortenEntity;
import com.musinsa.urlshortener.repository.UrlShortenRepository;
import com.musinsa.urlshortener.util.UrlShortenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@Service
@Transactional
public class UrlServiceImpl implements UrlService {

    @Autowired
    UrlShortenRepository urlShortenRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${app.host}")
    String host;

    @Override
    public ResponseEntity<ResponseDto> requestShortenUrl(UrlShortenRequestDto urlShortenRequestDto) {
        String originUrl =  UrlShortenUtil.removeHttp(urlShortenRequestDto.getUrl());

        return urlShortenRepository.findByOriginUrl(originUrl)
                .map(u -> {
                    u.setRequestCount(u.getRequestCount() + 1);
                    return ResponseEntity.status(CREATED).body(ResponseDto.builder()
                            .originUrl(u.getOriginUrl())
                            .shortenUrl(host + u.getShortUrl())
                            .requestCount(u.getRequestCount())
                            .build());
                })
                .orElseGet(() -> ResponseEntity.status(CREATED).body(createShortenUrl(originUrl)));
    }

    @Override
    public String redirectShortenUrl(String shortenUrl, HttpServletResponse response) {
       Optional<UrlShortenEntity> urlShortenEntity = urlShortenRepository.findByShortUrl(shortenUrl);

        if (urlShortenEntity.isPresent()) {
            return urlShortenEntity.get().getOriginUrl();
        } else {
            return "/error";
        }
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
        String shortUrl;
        while(true) {
            String tempShortUrl = UrlShortenUtil.generateUrl();
            if(validationShortUrl(tempShortUrl)) {
                shortUrl = tempShortUrl;
                break;
            }
        }

        return shortUrl;
    }

    private boolean validationShortUrl(String shortUrl) {
        return urlShortenRepository.findByShortUrl(shortUrl).isEmpty();
    }

}

