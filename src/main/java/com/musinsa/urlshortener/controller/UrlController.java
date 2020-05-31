package com.musinsa.urlshortener.controller;

import com.musinsa.urlshortener.dto.request.UrlShortenRequestDto;
import com.musinsa.urlshortener.dto.response.ResponseDto;
import com.musinsa.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class UrlController {

    @Autowired
    UrlService urlService;

    @GetMapping("{shortenUrl}")
    public String getUrlRedirect(@PathVariable("shortenUrl") String shortenUrl, HttpServletResponse response) {
        return "redirect:http://" + urlService.redirectShortenUrl(shortenUrl, response);
    }

    @PostMapping("url-shorten")
    @ResponseBody
    public ResponseEntity<ResponseDto> postUrlShorten(@RequestBody @Valid UrlShortenRequestDto urlShortenRequestDto) {
        return urlService.requestShortenUrl(urlShortenRequestDto);
    }

}
