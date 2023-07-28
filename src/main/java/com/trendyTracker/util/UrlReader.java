package com.trendyTracker.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class UrlReader {
    public static Set<String> getUrlContent(String url) throws IOException {
        TechListSingleton techListSingleton = TechListSingleton.getInstance();
        List<String> techList = techListSingleton.getTechList();

        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        ChromeOptions options = new ChromeOptions().addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        
        try {
            driver.get(url);
            WebElement bodyElement = driver.findElement(By.tagName("body"));
            String pageSource = bodyElement.getText();

            // 영어 단어를 추출하는 정규 표현식
            String regex = "\\b[a-zA-Z]+\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(pageSource);
            List<String> englishWords = new ArrayList<>();

            while (matcher.find()) 
                englishWords.add(matcher.group());

            return techList.stream()
                .filter(tech -> englishWords.stream()
                .anyMatch(word -> tech.equalsIgnoreCase(word)))
                .collect(Collectors.toSet());

        } finally {
            // 드라이버 종료
            driver.quit();
        }
    }
}
