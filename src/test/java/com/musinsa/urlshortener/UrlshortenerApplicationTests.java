package com.musinsa.urlshortener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.urlshortener.dto.request.UrlShortenRequestDto;
import com.musinsa.urlshortener.util.UrlShortenUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class UrlshortenerApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void requestUrlShortenTest() throws Exception {
        mockMvc.perform(post("/url-shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UrlShortenRequestDto("http://www.naver.com"))))
                .andExpect(status().isCreated())
                .andDo(print());
    }

}
