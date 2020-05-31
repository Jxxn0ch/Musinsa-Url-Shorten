package com.musinsa.urlshortener.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlShortenRequestDto {
    @NotEmpty
    @URL
    private String url;
}
