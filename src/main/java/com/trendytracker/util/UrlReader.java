package com.trendytracker.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendytracker.adaptors.cache.TechsCacheImpl;
import com.trendytracker.domain.job.tech.Tech;
import com.trendytracker.domain.job.recruit.dto.JobScrapInfoDto;

public class UrlReader {
    static Logger logger = LoggerFactory.getLogger(UrlReader.class);
    
    // 웹 페이지 로딩을 위한 대기 시간 (초 단위)
    private static final int PAGE_LOAD_TIMEOUT = 10;

    public static JobScrapInfoDto getUrlContent(String url, TechsCacheImpl redisManager) throws IOException {
        
        List<Tech> techList = redisManager.getTechList();
        WebDriver driver = setChromeDriver();
        
        try {
            driver.get(url);

            // 웹 페이지가 로딩될 때까지 대기
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            // title parsing 
            String title = getTitle(driver);
            
            // tech parsing
            WebElement bodyElement = driver.findElement(By.tagName("body"));
            String pageSource = bodyElement.getText();

            // English
            String regex = "\\b[a-zA-Z+#.-]+\\b";
            // C#,F#,C++,ASP.NET,.NET,Node.js,Vue.js
            String customRegex = "C#|F#|C++|\\bASP\\.NET\\b|\\b.NET\\b|\\bNode\\.js\\b|\\bVue\\.js\\b";

            Pattern pattern = Pattern.compile(regex);
            Pattern customPattern = Pattern.compile(customRegex);

            Matcher matcher = pattern.matcher(pageSource);
            Matcher customMatcher = customPattern.matcher(pageSource);

            Set<String> englishWords = new HashSet<String>();

            while (matcher.find()) 
                englishWords.add(matcher.group());

            while (customMatcher.find())
                englishWords.add(customMatcher.group());

            Set<Tech> techSet = techList.stream()
                .filter(tech -> englishWords.stream()
                .anyMatch(word -> tech.getTechName().equalsIgnoreCase(word)))
                .collect(Collectors.toSet());

            return new JobScrapInfoDto(title, techSet);

        } catch(Exception ex){
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
                serviceBuilder.usingDriverExecutable(new File("/opt/homebrew/bin/chromedriver"));

            // raspberryPi
            else if (osName.contains("linux") && osName.contains("arm")) 
                serviceBuilder.usingDriverExecutable(new File("/usr/bin/chromedriver"));

            // docker container
            else if (osName.contains("linux"))
                serviceBuilder.usingDriverExecutable(new File("/usr/local/bin/chromedriver"));
            
            ChromeDriverService service = serviceBuilder.build();
            service.start();
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // headless 모드 활성화
            options.addArguments("--no-sandbox"); // no-sandbox 옵션 추가
            options.addArguments("--disable-dev-shm-usage"); //  unknown error: session deleted because of page crash

            return new ChromeDriver(service, options);
        }
        catch(Exception ex){
            logger.error(ex.getMessage());
            return null;
        }
    }

    private static String getTitle(WebDriver driver) {
        var findElements = driver.findElements(By.tagName("h1"));
        var findElements2 = driver.findElements(By.tagName("h2"));

        if (findElements.size() != 0) 
            return  driver.findElement(By.tagName("h1")).getText();
        
        else if (findElements2.size() != 0) {
            return driver.findElement(By.tagName("h2")).getText();
        }

        return "";
    }
}
