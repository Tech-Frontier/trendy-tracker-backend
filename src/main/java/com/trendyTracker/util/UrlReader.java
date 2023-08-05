package com.trendyTracker.util;

import java.io.File;
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
        List<String> techList = TechListSingleton.getInstance().getTechList();
        WebDriver driver = setChromeDriver();
        
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
            driver.quit();
        }
    }

    private static WebDriver setChromeDriver() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) 
            System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        else if (osName.contains("linux") && osName.contains("arm")) 
            System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium-browser/chromedriver");

        else 
            throw new RuntimeException("chrome driver not exist");
        
        ChromeOptions options = new ChromeOptions().addArguments("--headless");
        return new ChromeDriver(options);
    }
}
