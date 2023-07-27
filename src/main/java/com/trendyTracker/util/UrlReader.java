package com.trendyTracker.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.client.RestTemplate;

public class UrlReader {
    static RestTemplate restTemplate = new RestTemplate();

    public static Set<String> getUrlContent(String url) throws IOException {
        // 기준 정보 Tech 추출
        TechListSingleton techListSingleton = TechListSingleton.getInstance();
        List<String> techList = techListSingleton.getTechList();
        Set<String> result = new HashSet<>();

        Document doc = Jsoup.connect(url).get();
        Elements div = doc.select("div");
        // div 조각 탐
        for (Element element : div) {
            String contents = element.text();

            System.out.println(contents);
            // 추출된 단어 출력 또는 원하는 작업 수행
            for (String tech : techList) {
                if(contents.toLowerCase().contains(tech.toLowerCase()))
                    result.add(tech);
            }
        }

        return result;
    }
}
