package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import scala.collection.Seq;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class FAQMatcher {

    public static void main(String[] args) throws Exception {
        // 새 질문
        String newQuestion = "은행 위치를 확인하려면 어떻게 해야 하나요?";

        // FAQ 데이터 로드
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, Object>> faqData = objectMapper.readValue(
                new File("tokenized_faq.json"),
                Map.class
        );

        // 새 질문 토큰화 (명사 추출)
        List<String> newQuestionTokens = tokenizeQuestion(newQuestion);

        // FAQ 데이터와 유사도 계산
        List<FAQSimilarity> similarities = calculateSimilarities(newQuestionTokens, faqData);

        // 상위 3개 유사 질문 출력
        similarities.stream()
                .limit(3)
                .forEach(similarity -> {
                    System.out.println("유사도: " + similarity.similarity);
                    System.out.println("질문: " + similarity.question);
                    System.out.println("답변: " + similarity.fullAnswer);
                    System.out.println("----");
                });
    }

    // 질문 토큰화 (명사만 추출)
    private static List<String> tokenizeQuestion(String question) {
        // Normalize the text
        CharSequence normalized = OpenKoreanTextProcessorJava.normalize(question);

        // Tokenize the normalized text
        Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);

        // Convert tokens to a List<KoreanTokenJava>
        List<KoreanTokenJava> tokenList = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);

        // Filter and extract nouns
        return tokenList.stream()
                .filter(token -> token.getPos().toString().equals("Noun")) // Extract only Nouns
                .map(KoreanTokenJava::getText) // Extract text from token
                .collect(Collectors.toList());
    }


    // FAQ 데이터와 유사도 계산
    private static List<FAQSimilarity> calculateSimilarities(
            List<String> newQuestionTokens,
            Map<String, Map<String, Object>> faqData
    ) {
        List<FAQSimilarity> results = new ArrayList<>();

        for (String question : faqData.keySet()) {
            List<String> faqQuestionTokens = (List<String>) faqData.get(question).get("questionTokens");
            double similarity = calculateJaccardSimilarity(newQuestionTokens, faqQuestionTokens);
            results.add(new FAQSimilarity(
                    question,
                    (String) faqData.get(question).get("fullAnswer"),
                    similarity
            ));
        }

        return results.stream()
                .sorted(Comparator.comparingDouble(FAQSimilarity::getSimilarity).reversed())
                .collect(Collectors.toList());
    }

    // 자카드 유사도 계산
    private static double calculateJaccardSimilarity(List<String> tokens1, List<String> tokens2) {
        Set<String> set1 = new HashSet<>(tokens1);
        Set<String> set2 = new HashSet<>(tokens2);

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    // 유사도 결과 저장 클래스
    static class FAQSimilarity {
        String question;
        String fullAnswer;
        double similarity;

        public FAQSimilarity(String question, String fullAnswer, double similarity) {
            this.question = question;
            this.fullAnswer = fullAnswer;
            this.similarity = similarity;
        }

        public double getSimilarity() {
            return similarity;
        }
    }
}

