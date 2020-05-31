package com.musinsa.urlshortener.controller;

import com.musinsa.urlshortener.dto.request.UrlShortenerRequestDto;
import com.musinsa.urlshortener.dto.response.UrlShortenerResponseDto;
import com.musinsa.urlshortener.exception.ShortenUrlNotFoundException;
import com.musinsa.urlshortener.service.UrlShortenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@Controller
@RequestMapping("/")
public class UrlShortenerController extends ResponseEntityExceptionHandler {

    @Autowired
    UrlShortenerService urlService;

    /**
     * URL 축소 API
     *
     * @param urlShortenRequestDto
     * @return ResponseEntity<UrlShortenerResponseDto>
     */
    @PostMapping("url-shortener")
    @ResponseBody
    public ResponseEntity<UrlShortenerResponseDto> postUrlShorten(@RequestBody @Valid UrlShortenerRequestDto urlShortenRequestDto) {
        return ResponseEntity.status(CREATED).body(urlService.requestShortenUrl(urlShortenRequestDto));
    }

    /**
     * 축소 URL 요청 시 원 주소로 리다이렉트
     *
     * @param shortenUrl
     * @return String
     */
    @GetMapping("{shortenUrl}")
    public String getUrlRedirect(@PathVariable("shortenUrl") String shortenUrl) {
        return "redirect:http://" + urlService.redirectShortenUrl(shortenUrl);
    }

    /**
     * 축소된 URL이 DB에 없을 경우 Exception
     *
     * @param exception
     * @return String
     */
    @ExceptionHandler
    private String shortenUrlNotFoundExceptionHandler(ShortenUrlNotFoundException exception) {
        log.error(exception.getMessage());
        return "redirect:/error";
    }

    /**
     * Valid 에러 메시지 정제
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("errorType", status.getReasonPhrase());
        body.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errorMessage", errors.get(0));

        return new ResponseEntity<>(body, headers, status);
    }
}