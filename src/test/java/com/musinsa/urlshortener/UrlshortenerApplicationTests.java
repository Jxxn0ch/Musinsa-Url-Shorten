package com.musinsa.urlshortener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.urlshortener.dto.request.UrlShortenRequestDto;
import com.musinsa.urlshortener.entity.UrlShortenEntity;
import com.musinsa.urlshortener.repository.UrlShortenRepository;
import com.musinsa.urlshortener.service.UrlShortenService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class UrlshortenerApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UrlShortenService urlShortenService;

    @Autowired
    UrlShortenRepository urlShortenRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        UrlShortenRequestDto urlShortenRequestDto = new UrlShortenRequestDto();
        urlShortenRequestDto.setUrl("http://www.naver.com");
        urlShortenService.requestShortenUrl(urlShortenRequestDto);
    }

    @AfterEach
    void afterEach() {
        urlShortenRepository.deleteAll();
    }

    @Test
    @DisplayName("메인 화면 - 성공")
    void index_success() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andDo(print());
    }

    @Test
    @DisplayName("Shorten URL 생성 - 성공")
    void requestUrlShorten_success() throws Exception {
        mockMvc.perform(post("/url-shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UrlShortenRequestDto("http://www.daum.net"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.originUrl").value("www.daum.net"))
                .andExpect(jsonPath("$.shortenUrl").isNotEmpty())
                .andExpect(jsonPath("$.requestCount").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("Shorten URL 생성 - 실패")
    void requestUrlShorten_fail() throws Exception {
        mockMvc.perform(post("/url-shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UrlShortenRequestDto("www.daum.net"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Shorten URL 리다이렉트 - 성공")
    void redirectShortenUrl_success() throws Exception {
        UrlShortenEntity urlShortenEntity = urlShortenRepository.findByOriginUrl("www.naver.com")
                .orElseThrow(() -> new NotFoundException("Shorten URL Not Found"));

        mockMvc.perform(get("/" + urlShortenEntity.getShortUrl()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://www.naver.com"))
                .andDo(print());
    }

    @Test
    @DisplayName("Shorten URL 리다이렉트 - 실패")
    void redirectShortenUrl_fail() throws Exception {
        mockMvc.perform(get("/shorten-url"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"))
                .andDo(print());
    }

}