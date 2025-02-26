package project.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dto.AnalyzeResultResponse;
import project.payload.status.ErrorStatus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AnalysisService {
    private Map<String, Integer> spamWords;
    private Map<String, Integer> phishingWords;

    @PostConstruct
    public void init() {
        spamWords = loadWords("/words/spam_words.txt");
        phishingWords = loadWords("/words/phishing_words.txt");
    }

    public boolean isInputExists(String inputText) {
        return inputText == null || inputText.trim().isEmpty();
    }

    public AnalyzeResultResponse.ResultMessage analyzeText(String inputText) {

        int spamScore = 0;
        int phishingScore = 0;

        for (String word : spamWords.keySet()) {
            int count = countOccurrences(inputText.toLowerCase(), word.toLowerCase());
            spamScore += spamWords.get(word) * count;
        }

        spamScore+=shapeSpecialCharacterScore(inputText);                                           //특수문자 가중처리(메모장에 적으면 불러올때 과부하 발생하여 java내부에서 처리)
        phishingScore+=shapeSpecialCharacterScore(inputText)+circleSpecialCharacterScore(inputText);//특수문자 가중처리(메모장에 적으면 불러올때 과부하 발생하여 java내부에서 처리)

        for (String word : phishingWords.keySet()) {
            int count = countOccurrences(inputText.toLowerCase(), word.toLowerCase());
            phishingScore += phishingWords.get(word) * count;
        }

        String spamMessage;
        if (spamScore < 30) {
            spamMessage = "낮음";
        } else if (spamScore < 60) {
            spamMessage = "있음";
        } else if (spamScore < 90) {
            spamMessage = "높음";
        } else {
            spamMessage = "확실";
        }

        String phishingMessage;
        if (phishingScore < 30) {
            phishingMessage = "낮음";
        } else if (phishingScore < 60) {
            phishingMessage = "있음";
        } else if (phishingScore < 90) {
            phishingMessage = "높음";
        } else {
            phishingMessage = "확실";
        }


        //스팸과 피싱중에서 더 가까운 것(점수가 큰 것)을 반환
        if (spamScore > phishingScore) {
            return AnalyzeResultResponse.ResultMessage.builder()
                    .kind("spam")
                    .state(spamMessage)
                    .build();
        } else {
            return AnalyzeResultResponse.ResultMessage.builder()
                    .kind("phishing")
                    .state(phishingMessage)
                    .build();
        }

    }

    private int countOccurrences(String text, String word) {
        text = cleanText(text);
        word = cleanText(word);

        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf(word, idx)) != -1) {
            count++;
            //System.out.println(word);
            idx += word.length();
        }
        return count;
    }

    private String cleanText(String text) {
        return text.replaceAll("[^가-힣a-z0-9.①-ⓩ★☆♡♥●◆♠♣◇■▣▷☞☜▶▼◀▲◈☎【】↑↓]", "");
        //특수문자,띄어쓰기 제거(영어,한국어,숫자만 남김)
    }
    private Map<String, Integer> loadWords(String filePath) {
        Map<String, Integer> words = new HashMap<>();
        try (
                InputStream is = getClass().getResourceAsStream(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String word = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    words.put(word, score);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }

    private int shapeSpecialCharacterScore(String text) {
        String specialCharRegex = "[★☆♡♥●◆♠♣◇■▣▷☞☜▶▼◀▲◈☎【】↑↓]";

        int count = 0;
        for (char c : text.toCharArray()) {
            if (String.valueOf(c).matches(specialCharRegex)) {
                count++;
            }
        }
        return count*7;
    }
    private int circleSpecialCharacterScore(String text) {
        String specialCharRegex = "[①-ⓩ]";

        int count = 0;
        for (char c : text.toCharArray()) {
            if (String.valueOf(c).matches(specialCharRegex)) {
                count++;
            }
        }
        return count*60;
    }
}
