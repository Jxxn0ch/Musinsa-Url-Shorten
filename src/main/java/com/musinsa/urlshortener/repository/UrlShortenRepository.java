package com.musinsa.urlshortener.repository;

import com.musinsa.urlshortener.entity.UrlShortenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlShortenRepository extends JpaRepository<UrlShortenEntity, Long> {

    UrlShortenEntity findByOriginUrl(String url);

    UrlShortenEntity findByShortUrl(String shortUrl);
}
