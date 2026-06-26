package com.example.urlshortener.controller;

import com.example.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://url-shortener-frontend-liart-eight.vercel.app"
})
public class UrlController {

    private final UrlService urlService;

    @Value("${app.base-url}")
    private String baseUrl;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    // Create Short URL
    @PostMapping("/api/shorten")
    public Map<String, String> shortenUrl(@RequestBody Map<String, String> request) {

        String longUrl = request.get("longUrl");
        String shortCode = urlService.shortenUrl(longUrl);

        return Map.of(
                "shortCode", shortCode,
                "shortUrl", baseUrl + "/" + shortCode
        );
    }

    // Redirect to Original URL
    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode,
                         HttpServletResponse response) throws IOException {

        String longUrl = urlService.getOriginalUrl(shortCode);
        response.sendRedirect(longUrl);
    }
}