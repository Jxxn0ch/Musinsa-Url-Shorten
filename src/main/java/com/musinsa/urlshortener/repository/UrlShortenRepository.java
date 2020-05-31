package com.musinsa.urlshortener.repository;

import com.musinsa.urlshortener.entity.UrlShortenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlShortenRepository extends JpaRepository<UrlShortenEntity, Long> {
    Optional<UrlShortenEntity> findByOriginUrl(String url);
    Optional<UrlShortenEntity> findByShortUrl(String shortUrl);
}
