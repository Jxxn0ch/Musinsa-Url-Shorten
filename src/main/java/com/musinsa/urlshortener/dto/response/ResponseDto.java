package com.musinsa.urlshortener.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class ResponseDto {
    private int requestCount;
    private String originUrl;
    private String shortenUrl;
}
