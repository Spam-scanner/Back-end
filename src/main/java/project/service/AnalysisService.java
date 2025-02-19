package project.service;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class AnalysisService {

    public Map<String, Object> analyzeText(String inputText) {
        int spamScore = 0;
        int phishingScore = 0;

        // [스팸 단어 설정] - 추후 수정 가능하도록 분리
        Map<String, Integer> spamWords = new HashMap<>();
        spamWords.put("word1", 10);
        spamWords.put("word2", 20);
        // 추가 스팸 단어와 점수 설정 가능

        // [피싱 단어 설정] - 추후 수정 가능하도록 분리
        Map<String, Integer> phishingWords = new HashMap<>();
        phishingWords.put("phish1", 15);
        phishingWords.put("phish2", 25);
        // 추가 피싱 단어와 점수 설정 가능

        // 스팸 점수 계산
        for (String word : spamWords.keySet()) {
            int count = countOccurrences(inputText.toLowerCase(), word.toLowerCase());
            spamScore += spamWords.get(word) * count;
        }

        // 피싱 점수 계산
        for (String word : phishingWords.keySet()) {
            int count = countOccurrences(inputText.toLowerCase(), word.toLowerCase());
            phishingScore += phishingWords.get(word) * count;
        }

        // 결과 메시지 결정 (스팸)
        String spamMessage;
        if (spamScore < 30) {
            spamMessage = "스팸이 아닙니다.";
        } else if (spamScore < 60) {
            spamMessage = "스팸일 가능성이 있습니다.";
        } else if (spamScore < 90) {
            spamMessage = "스팸일 가능성이 높습니다.";
        } else {
            spamMessage = "스팸이 확실합니다.";
        }

        // 결과 메시지 결정 (피싱)
        String phishingMessage;
        if (phishingScore < 30) {
            phishingMessage = "피싱이 아닙니다.";
        } else if (phishingScore < 60) {
            phishingMessage = "피싱일 가능성이 있습니다.";
        } else if (phishingScore < 90) {
            phishingMessage = "피싱일 가능성이 높습니다.";
        } else {
            phishingMessage = "피싱이 확실합니다.";
        }

        // 결과 값을 Map에 담아서 반환
        Map<String, Object> result = new HashMap<>();
        result.put("spamScore", spamScore);
        result.put("spamMessage", spamMessage);
        result.put("phishingScore", phishingScore);
        result.put("phishingMessage", phishingMessage);

        return result;
    }

    // 입력 문자열에서 특정 단어가 몇 번 등장하는지 계산하는 헬퍼 메서드
    private int countOccurrences(String text, String word) {
        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf(word, idx)) != -1) {
            count++;
            idx += word.length();
        }
        return count;
    }
}

