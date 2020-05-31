package com.musinsa.urlshortener.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlShortenerRequestDto {
    @NotEmpty
    @URL(message = "유효하지 않은 url 입니다.")
    private String url;
}
