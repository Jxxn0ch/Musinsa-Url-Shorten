package com.musinsa.urlshortener.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.urlshortener.dto.request.UrlShortenRequestDto;
import com.musinsa.urlshortener.dto.response.ResponseDto;
import com.musinsa.urlshortener.entity.UrlShortenEntity;
import com.musinsa.urlshortener.repository.UrlShortenRepository;
import com.musinsa.urlshortener.util.UrlShortenUtil;
import com.musinsa.urlshortener.vo.UrlShortenVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public ResponseEntity<ResponseDto> requestShortenUrl(UrlShortenRequestDto urlShortenRequestDto) {
        String originUrl =  UrlShortenUtil.removeHttp(urlShortenRequestDto.getUrl());

        Optional<UrlShortenVo> urlShortenVo = objectMapper.convertValue(
                urlShortenRepository.findByOriginUrl(originUrl), new TypeReference<>() {});

        return urlShortenVo.
                map(vo -> ResponseEntity.status(CREATED).body(updateRequestCount(vo)))
                .orElseGet(() -> ResponseEntity.status(CREATED).body(createShortenUrl(originUrl)));
    }

    @Override
    public String redirectShortenUrl(String shortenUrl, HttpServletResponse response) {
        Optional<UrlShortenVo> urlShortenVo = objectMapper.convertValue(
                urlShortenRepository.findByShortUrl(shortenUrl), new TypeReference<>() {});

        if (urlShortenVo.isPresent()) {
            return urlShortenVo.get().getOriginUrl();
        } else {
            return "/error";
        }
    }

    private ResponseDto createShortenUrl(String originUrl) {
        String shortUrl = generateShortUrl();

        urlShortenRepository.save(UrlShortenEntity.builder()
                                    .originUrl(originUrl)
                                    .shortUrl(shortUrl)
                                    .requestCount(1)
                                    .build());

        return ResponseDto.builder()
                .originUrl(originUrl)
                .shortenUrl("http://localhost/" + shortUrl)
                .requestCount(1)
                .build();
    }

    private ResponseDto updateRequestCount(UrlShortenVo urlShortenVo) {
        urlShortenVo.setRequestCount(urlShortenVo.getRequestCount() + 1);
        urlShortenRepository.save(objectMapper.convertValue(urlShortenVo, UrlShortenEntity.class));

        return ResponseDto.builder()
                .originUrl(urlShortenVo.getOriginUrl())
                .shortenUrl("http://localhost/" + urlShortenVo.getShortUrl())
                .requestCount(urlShortenVo.getRequestCount())
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
        Optional<UrlShortenVo> urlShortenVo = objectMapper.convertValue(
                urlShortenRepository.findByShortUrl(shortUrl), new TypeReference<>() {});

        return urlShortenVo.isEmpty();
    }

}

