package com.jeevitha.url_shortener.controller;

import com.jeevitha.url_shortener.dto.AnalyticsResponse;
import com.jeevitha.url_shortener.dto.TopUrlResponse;
import com.jeevitha.url_shortener.dto.UrlRequest;
import com.jeevitha.url_shortener.dto.UrlResponse;
import com.jeevitha.url_shortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/api/urls")
    public UrlResponse creatingShortUrl(@RequestBody UrlRequest urlRequest){
        return urlService.createShortUrl(urlRequest);
    }

    @GetMapping("/r/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION,originalUrl).build();
    }

    @GetMapping("/api/urls/{shortCode}/analytics")
    public AnalyticsResponse getAnalytics(@PathVariable String shortCode) {
        return urlService.getAnalytics(shortCode);
    }

    @GetMapping("/api/urls/top")
    public List<TopUrlResponse> getTopUrls(){
        return urlService.getTopUrls();
    }

    @GetMapping("/api/urls/{shortCode}/status")
    public ResponseEntity<String> checkStatus(
            @PathVariable String shortCode) {

        urlService.validateUrl(shortCode);

        return ResponseEntity.ok("ACTIVE");
    }
}
