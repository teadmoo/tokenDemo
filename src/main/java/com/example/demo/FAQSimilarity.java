package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FAQSimilarity {

    // JSON 파일에서 토큰화된 FAQ 데이터 로드
    public static Map<String, Map<String, Object>> loadTokenizedFAQ(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Map.class));
    }

    // 새로운 질문을 토큰화하여 기존 FAQ와 비교
    public static List<QuestionSimilarity> getTop3SimilarQuestions(String newQuestion, Map<String, Map<String, Object>> tokenizedFAQ) {
        List<String> newQuestionTokens = FAQTokenizer.tokenizeNewQuestion(newQuestion);  // 새로운 질문 토큰화

        // 기존 질문과의 유사도를 계산하여 점수 매기기
        Map<String, Double> similarityScores = new HashMap<>();
        for (String question : tokenizedFAQ.keySet()) {
            Map<String, Object> entry = tokenizedFAQ.get(question);
            Object questionTokensObj = entry.get("questionTokens");

            // 타입 확인 후 캐스팅
            if (questionTokensObj instanceof List<?>) {
                // 캐스팅이 안전하다고 판단되면, 실제 List<String>로 캐스팅
                @SuppressWarnings("unchecked")
                List<String> questionTokens = (List<String>) questionTokensObj;  // 안전한 캐스팅
                double similarity = calculateCosineSimilarity(newQuestionTokens, questionTokens);
                similarityScores.put(question, similarity);
            } else {
                System.out.println("타입 불일치: questionTokens");
            }
        }

        // 유사도가 높은 3개의 질문 선택
        return similarityScores.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))  // 내림차순 정렬
                .limit(3)  // 상위 3개 질문만 추출
                .map(entry -> {
                    String question = entry.getKey();
                    double similarity = entry.getValue();
                    String answer = (String) tokenizedFAQ.get(question).get("fullAnswer");  // 답변 추출
                    return new QuestionSimilarity(question, similarity, answer);
                })
                .collect(Collectors.toList());
    }

    // 새로운 질문을 토큰화하여 기존 FAQ와 비교
//    public static List<QuestionSimilarity> getTop3SimilarQuestions(String newQuestion, Map<String, Map<String, Object>> tokenizedFAQ) {
//        List<String> newQuestionTokens = FAQTokenizer.tokenizeNewQuestion(newQuestion);  // 새로운 질문 토큰화
//
//        // 기존 질문과의 유사도를 계산하여 점수 매기기
//        Map<String, Double> similarityScores = new HashMap<>();
//        for (String question : tokenizedFAQ.keySet()) {
//            Map<String, Object> entry = tokenizedFAQ.get(question);
//
//            // 질문과 답변의 토큰을 각각 추출
//            List<String> questionTokens = (List<String>) entry.get("questionTokens");
//            List<String> answerTokens = (List<String>) entry.get("answerTokens");
//
//            // Cosine Similarity 계산 (질문과 답변 모두 비교)
//            double questionSimilarity = calculateCosineSimilarity(newQuestionTokens, questionTokens);
//            double answerSimilarity = calculateCosineSimilarity(newQuestionTokens, answerTokens);
//
//            // 질문과 답변의 유사도 합산 (또는 다른 방식으로 결합 가능)
//            double totalSimilarity = (questionSimilarity + answerSimilarity) / 2.0;
//
//            similarityScores.put(question, totalSimilarity);
//        }
//
//        // 유사도가 높은 3개의 질문 선택
//        return similarityScores.entrySet().stream()
//                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))  // 내림차순 정렬
//                .limit(3)  // 상위 3개 질문만 추출
//                .map(entry -> {
//                    String question = entry.getKey();
//                    double similarity = entry.getValue();
//                    String answer = (String) tokenizedFAQ.get(question).get("fullAnswer");  // 답변 추출
//                    return new QuestionSimilarity(question, similarity, answer);
//                })
//                .collect(Collectors.toList());
//    }


    // 두 개의 토큰 리스트 사이의 Cosine Similarity 계산
    private static double calculateCosineSimilarity(List<String> tokens1, List<String> tokens2) {
        Set<String> allTokens = new HashSet<>();
        allTokens.addAll(tokens1);
        allTokens.addAll(tokens2);

        // 각 토큰의 빈도를 계산
        Map<String, Integer> vector1 = new HashMap<>();
        Map<String, Integer> vector2 = new HashMap<>();

        for (String token : allTokens) {
            vector1.put(token, Collections.frequency(tokens1, token));
            vector2.put(token, Collections.frequency(tokens2, token));
        }

        // Cosine Similarity 계산
        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (String token : allTokens) {
            dotProduct += vector1.get(token) * vector2.get(token);
            norm1 += Math.pow(vector1.get(token), 2);
            norm2 += Math.pow(vector2.get(token), 2);
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));  // Cosine Similarity 공식
    }

    // 유사도와 답변을 담을 클래스
    public static class QuestionSimilarity {
        private String question;
        private double similarity;
        private String answer;

        public QuestionSimilarity(String question, double similarity, String answer) {
            this.question = question;
            this.similarity = similarity;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public double getSimilarity() {
            return similarity;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
