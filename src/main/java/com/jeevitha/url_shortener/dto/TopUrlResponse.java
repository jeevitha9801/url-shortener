package com.jeevitha.url_shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopUrlResponse {
    private String shortCode;
    private String originalUrl;
    private Long clickCount;
}
