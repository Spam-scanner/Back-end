package project.service;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalysisService {
    private Map<String, Integer> spamWords;
    private Map<String, Integer> phishingWords;

    @PostConstruct
    public void init() {
        spamWords = loadWords("/words/spam_words.txt");
        phishingWords = loadWords("/words/phishing_words.txt");
    }

    public Map<String, Object> analyzeText(String inputText) {
        int spamScore = 0;
        int phishingScore = 0;

        for (String word : spamWords.keySet()) {
            int count = countOccurrences(inputText.toLowerCase(), word.toLowerCase());
            spamScore += spamWords.get(word) * count;
        }

        for (String word : phishingWords.keySet()) {
            int count = countOccurrences(inputText.toLowerCase(), word.toLowerCase());
            phishingScore += phishingWords.get(word) * count;
        }

        String spamMessage;
        if (spamScore < 30) {
            spamMessage = "안심";
        } else if (spamScore < 60) {
            spamMessage = "주의";
        } else if (spamScore < 90) {
            spamMessage = "위험";
        } else {
            spamMessage = "심각";
        }

        String phishingMessage;
        if (phishingScore < 30) {
            phishingMessage = "안심";
        } else if (phishingScore < 60) {
            phishingMessage = "주의";
        } else if (phishingScore < 90) {
            phishingMessage = "위험";
        } else {
            phishingMessage = "심각";
        }

        Map<String, Object> result = new HashMap<>();
        result.put("spamMessage", spamMessage);
        result.put("phishingMessage", phishingMessage);

        return result;
    }

    private int countOccurrences(String text, String word) {
        text = cleanText(text);
        word = cleanText(word);

        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf(word, idx)) != -1) {
            count++;
            idx += word.length();
        }
        return count;
    }

    private String cleanText(String text) {
        return text.replaceAll("[^가-힣a-zA-Z0-9.]", ""); //특수문자,띄어쓰기 제거(영어,한국어,숫자만 남김)
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
}
