package com.musinsa.urlshortener.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private int requestCount;
    private String originUrl;
    private String shortenUrl;
}
