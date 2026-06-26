package com.example.urlshortener.service;

import com.example.urlshortener.entity.Url;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // 🔹 Create short URL (with duplicate check)
    public String shortenUrl(String longUrl) {

        Optional<Url> existingUrl = urlRepository.findByLongUrl(longUrl);

        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortCode();
        }

        String shortCode = generateUniqueShortCode();

        Url url = new Url();
        url.setLongUrl(longUrl);
        url.setShortCode(shortCode);

        urlRepository.save(url);

        return shortCode;
    }

    // 🔹 Get original URL
    public String getOriginalUrl(String shortCode) {

        return urlRepository.findByShortCode(shortCode)
                .map(Url::getLongUrl)
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }

    // 🔹 Ensure unique short code
    private String generateUniqueShortCode() {

        String code;

        do {
            code = generateShortCode();
        } while (urlRepository.findByShortCode(code).isPresent());

        return code;
    }

    // 🔹 Random generator
    private String generateShortCode() {

        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }
}