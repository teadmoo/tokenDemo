//package com.example.demo;
//
//import org.openkoreantext.processor.KoreanTokenJava;
//import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
//import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
//import scala.collection.Seq;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class FAQSearcher {
//    private final Map<String, Map<String, Object>> faqData;
//
//    // 제외할 단어와 합성어 리스트 정의
//    private static final List<String> EXCLUDED_TOKENS = List.of("질문"); // 제외할 단어
//    private static final List<String> COMPOSITE_TOKENS = List.of("하나원큐"); // 합성어 리스트
//
//    public FAQSearcher(Map<String, Map<String, Object>> faqData) {
//        this.faqData = faqData;
//    }
//
//    public List<String> findSimilarAnswers(String query) {
//        // 1. 입력 질문을 토큰화
//        List<String> queryTokens = tokenizeText(query);
//
//        // 2. 각 FAQ 항목과의 유사도 계산
//        Map<String, Double> similarities = new HashMap<>();
//
//        for (Map.Entry<String, Map<String, Object>> entry : faqData.entrySet()) {
//            String question = entry.getKey();
//            @SuppressWarnings("unchecked")
//            List<String> questionTokens = extractNounsFromTokens((List<String>) entry.getValue().get("questionTokens"));
//            @SuppressWarnings("unchecked")
//            List<String> answerTokens = extractNounsFromTokens((List<String>) entry.getValue().get("answerTokens"));
//
//            // 질문 토큰과 답변 토큰을 합쳐서 비교
//            Set<String> faqTokens = new HashSet<>();
//            faqTokens.addAll(questionTokens);
//            faqTokens.addAll(answerTokens);
//
//            // 자카드 유사도 계산
//            double similarity = calculateJaccardSimilarity(queryTokens, faqTokens);
//            similarities.put(question, similarity);
//        }
//
//        // 3. 유사도가 높은 상위 3개 답변 반환
//        return similarities.entrySet().stream()
//                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
//                .limit(3)
//                .map(entry -> {
//                    String question = entry.getKey();
//                    String answer = (String) faqData.get(question).get("fullAnswer");
//                    return String.format("질문: %s\n답변: %s\n유사도: %.2f\n",
//                            question, answer, entry.getValue());
//                })
//                .collect(Collectors.toList());
//    }
//
//    private List<String> tokenizeText(String text) {
//        // OpenKoreanText를 사용하여 토큰화
//        CharSequence normalized = OpenKoreanTextProcessorJava.normalize(text);
//        Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
//
//        // 반환된 tokens가 null인지 체크
//        if (tokens == null) {
//            System.out.println("토큰화 결과가 null입니다.");
//            return new ArrayList<>();  // null일 경우 빈 리스트 반환
//        }
//
//        // 토큰화 결과 출력 (디버깅용)
//        System.out.println("토큰화된 토큰들: " + tokens);
//
//        // 명사만 추출 후 필터링
//        List<String> nounTokens = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens).stream()
//                .filter(token -> token.getPos().toString().equals("Noun")) // 명사만 추출
//                .map(KoreanTokenJava::toString)
//                .collect(Collectors.toList());
//
//        // 필터링 및 합성어 처리
//        return filterTokens(nounTokens);
//    }
//
//    private List<String> extractNounsFromTokens(List<String> tokens) {
//        // 빈 리스트나 null 처리
//        if (tokens == null || tokens.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        // "(Noun: x, y)" 형식의 토큰에서 실제 단어만 추출
//        return tokens.stream()
//                .filter(token -> token.contains("Noun"))  // "Noun"이 포함된 토큰만 필터링
//                .map(token -> token.split("\\(")[0].trim())  // 단어 추출
//                .collect(Collectors.toList());
//    }
//
//    // Filters tokens to remove unwanted words like "질문" and preserve specific words
//    private List<String> filterTokens(List<String> tokens) {
//        return tokens.stream()
//                .map(token -> token.split("\\(")[0].trim()) // 태그 제거 및 공백 제거
//                .map(String::toLowerCase) // 소문자로 변환
//                .filter(token -> !EXCLUDED_TOKENS.contains(token)) // "질문" 제외
//                .map(token -> handleCompositeTokens(token)) // 합성어 처리
//                .filter(token -> !token.isEmpty()) // 빈 값 필터링
//                .collect(Collectors.toList());
//    }
//
//    // Handle composite tokens like "하나원큐"
//    private String handleCompositeTokens(String token) {
//        // If the token is part of the composite list, return it as is
//        for (String composite : COMPOSITE_TOKENS) {
//            if (token.contains(composite)) {
//                return composite; // Return the whole composite token
//            }
//        }
//        return token; // Otherwise, return the token as is
//    }
//
//    private double calculateJaccardSimilarity(List<String> tokens1, Collection<String> tokens2) {
//        Set<String> set1 = new HashSet<>(tokens1);
//        Set<String> set2 = new HashSet<>(tokens2);
//
//        Set<String> intersection = new HashSet<>(set1);
//        intersection.retainAll(set2);
//
//        Set<String> union = new HashSet<>(set1);
//        union.addAll(set2);
//
//        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
//    }
//}
