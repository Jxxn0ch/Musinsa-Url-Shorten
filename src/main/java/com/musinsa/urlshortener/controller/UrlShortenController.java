package com.musinsa.urlshortener.controller;

import com.musinsa.urlshortener.dto.request.UrlShortenRequestDto;
import com.musinsa.urlshortener.dto.response.ResponseDto;
import com.musinsa.urlshortener.exception.ShortenUrlNotFoundException;
import com.musinsa.urlshortener.service.UrlShortenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@Controller
@RequestMapping("/")
public class UrlShortenController {

    @Autowired
    UrlShortenService urlService;

    @PostMapping("url-shorten")
    @ResponseBody
    public ResponseEntity<ResponseDto> postUrlShorten(@RequestBody @Valid UrlShortenRequestDto urlShortenRequestDto) {
        return ResponseEntity.status(CREATED).body(urlService.requestShortenUrl(urlShortenRequestDto));
    }

    @GetMapping("{shortenUrl}")
    public String getUrlRedirect(@PathVariable("shortenUrl") String shortenUrl) {
        return "redirect:http://" + urlService.redirectShortenUrl(shortenUrl);
    }
    @ExceptionHandler
    private String shortenUrlNotFoundExceptionHandler(ShortenUrlNotFoundException exception) {
        log.error(exception.getMessage());
        return "redirect:/error";
    }

}