package com.musinsa.urlshortener.repository;

import com.musinsa.urlshortener.entity.UrlShortenerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlShortenerRepository extends JpaRepository<UrlShortenerEntity, Long> {
    Optional<UrlShortenerEntity> findByOriginUrl(String url);
    Optional<UrlShortenerEntity> findByShortUrl(String shortUrl);
}
