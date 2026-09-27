package com.jeevitha.url_shortener.service;

import com.jeevitha.url_shortener.dto.AnalyticsResponse;
import com.jeevitha.url_shortener.dto.TopUrlResponse;
import com.jeevitha.url_shortener.dto.UrlRequest;
import com.jeevitha.url_shortener.dto.UrlResponse;
import com.jeevitha.url_shortener.entity.UrlMapping;
import com.jeevitha.url_shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlResponse createShortUrl(UrlRequest request) {
        if (request.getOriginalUrl() == null || request.getOriginalUrl().isBlank()) {
            throw new RuntimeException("URL cannot be empty");
        }

        Optional<UrlMapping> existingUrl = urlRepository.findByOriginalUrl(request.getOriginalUrl());

        if (existingUrl.isPresent()) {
            UrlMapping existing = existingUrl.get();
            if (request.getCustomAlias() != null && !request.getCustomAlias().isBlank()) {
                if (existing.getShortCode().equals(request.getCustomAlias())) {
                    return new UrlResponse("http://localhost:8080/r/" + existing.getShortCode());
                }
                throw new RuntimeException("URL already exists with short code: " + existing.getShortCode());
            }
            return new UrlResponse("http://localhost:8080/r/" + existing.getShortCode());
        }

        String shortCode;

        if (request.getCustomAlias() != null && !request.getCustomAlias().isBlank()) {
            if (urlRepository.findByShortCode(request.getCustomAlias()).isPresent()) {
                throw new RuntimeException("Alias already exists");
            }
            shortCode = request.getCustomAlias();
        } else {
            shortCode = UUID.randomUUID()
                    .toString()
                    .substring(0, 6);
        }

        UrlMapping urlMapping = new UrlMapping();

        urlMapping.setShortCode(shortCode);
        urlMapping.setOriginalUrl(request.getOriginalUrl());
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setClickCount(0L);
        urlMapping.setExpiresAt(LocalDateTime.now().plusDays(7));

        urlRepository.save(urlMapping);

        return new UrlResponse("http://localhost:8080/r/" + shortCode);
    }

    public String getOriginalUrl(String shortCode){
        UrlMapping urlMapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(()-> new RuntimeException("Short URL not found"));
        if (urlMapping.getExpiresAt() != null && LocalDateTime.now().isAfter(urlMapping.getExpiresAt())) {
            throw new RuntimeException("Short URL has expired");
        }
        urlMapping.setClickCount(urlMapping.getClickCount()+1);
        urlRepository.save(urlMapping);
        return urlMapping.getOriginalUrl();
    }

    public AnalyticsResponse getAnalytics(String shortCode) {

        UrlMapping urlMapping = urlRepository
                .findByShortCode(shortCode)
                .orElseThrow(() ->
                        new RuntimeException("Short URL not found"));

        return new AnalyticsResponse(
                urlMapping.getShortCode(),
                urlMapping.getOriginalUrl(),
                urlMapping.getClickCount(),
                urlMapping.getCreatedAt(),
                urlMapping.getExpiresAt()
        );
    }

    public List<TopUrlResponse> getTopUrls(){
        return urlRepository.findTop3ByOrderByClickCountDesc().stream()
                .map(url -> new TopUrlResponse(
                        url.getShortCode(),
                        url.getOriginalUrl(),
                        url.getClickCount()
                )).toList();
    }
    public void validateUrl(String shortCode){

        UrlMapping urlMapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new RuntimeException("Short URL not found"));

        if (urlMapping.getExpiresAt() != null &&
                LocalDateTime.now().isAfter(urlMapping.getExpiresAt())) {

            throw new RuntimeException("Short URL has expired");
        }
    }

}
