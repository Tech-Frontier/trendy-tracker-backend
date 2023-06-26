package com.trendyTracker.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UrlReader {
    static RestTemplate restTemplate = new RestTemplate();

    public static String getUrlContent(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String htmlString = response.getBody();
        Document document = Jsoup.parse(htmlString);

        return document.text();
    }
}
