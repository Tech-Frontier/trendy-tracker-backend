package com.trendyTracker.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.trendyTracker.util.UrlReader;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AnalyzeTextService {
    /*
     * KOMORAN 한글 형태소 분석 로직 ( Legacy )
     */
    private Map<String, Set<String>> nounMap = new HashMap<String, Set<String>>();

    private Set<String> koreanSet = new HashSet<String>();
    private Set<String> englishSet = new HashSet<String>();

    public Map<String, Set<String>> analyzeText(String url) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        String urlContent = UrlReader.getUrlContent(url);

        KomoranResult analyzeResultList = komoran.analyze(urlContent);
        List<String> nouns = analyzeResultList.getNouns();

        for (String noun : nouns) {
            if (isEnglishWord(noun))
                englishSet.add(noun);
            else
                koreanSet.add(noun);
        }

        nounMap.put("korean", koreanSet);
        nounMap.put("english", englishSet);
        return nounMap;
    }

    private static boolean isEnglishWord(String word) {
        return word.matches("[a-zA-Z]+");
    }
}
