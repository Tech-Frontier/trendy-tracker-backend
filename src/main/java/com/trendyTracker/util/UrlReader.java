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
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlReader {
    static Logger logger = LoggerFactory.getLogger(UrlReader.class);

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

        } catch(Exception ex){
            logger.info("getUrlContent");
            logger.error(ex.getMessage());
            return null;
        }
         finally {
            driver.quit();
        }
    }

    private static WebDriver setChromeDriver() {
        try{
            String osName = System.getProperty("os.name").toLowerCase();
            ChromeDriverService.Builder serviceBuilder = new ChromeDriverService.Builder();
        
            // mac
            if (osName.contains("mac")) 
                serviceBuilder.usingDriverExecutable(new File("/usr/local/bin/chromedriver"));

            // raspberryPi
            else if (osName.contains("linux") && osName.contains("arm")) 
                serviceBuilder.usingDriverExecutable(new File("/usr/lib/chromium-browser/chromedriver"));

            // docker container
            else if (osName.contains("linux"))
                serviceBuilder.usingDriverExecutable(new File("/usr/local/bin/chromedriver"));
            
            ChromeDriverService service = serviceBuilder.usingPort(9515).build();
            service.start();
            
            ChromeOptions options = new ChromeOptions().addArguments("--headless");
            String[] allowedIps = {"172.17.*.*"};
            options.addArguments("--allowed-ips=" + String.join(",", allowedIps));
            options.addArguments("--port=9515");

            return new ChromeDriver(service, options);
        }
        catch(Exception ex){
            logger.info("setChromeDriver");
            logger.error(ex.getMessage());
            return null;
        }
    }
}
