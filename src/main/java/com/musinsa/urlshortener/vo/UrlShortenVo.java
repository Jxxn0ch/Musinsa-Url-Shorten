package com.musinsa.urlshortener.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlShortenVo {
    private Long id;
    private int requestCount;
    private String originUrl;
    private String shortUrl;
    private Date createdDate;
    private Date lastModifiedDate;
}
