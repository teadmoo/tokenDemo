package com.example.demo;


import java.util.*;

public class FAQApp {
    public static void main(String[] args) {
        try {
            // Step 1: 기존 FAQ 데이터 로드
            Map<String, Map<String, Object>> tokenizedData = FAQSimilarity.loadTokenizedFAQ("tokenized_faq.json");

            // Step 2: 새로운 질문
            String newQuestion = "하나은행 홈페이지 이용 방법";

            // Step 3: 유사한 질문 3개 찾기
            List<FAQSimilarity.QuestionSimilarity> top3SimilarQuestions = FAQSimilarity.getTop3SimilarQuestions(newQuestion, tokenizedData);

            // Step 4: 결과 출력
            System.out.println("가장 유사한 3개의 질문과 답변:");
            for (FAQSimilarity.QuestionSimilarity similarity : top3SimilarQuestions) {
                System.out.println("질문: " + similarity.getQuestion());
                System.out.println("유사도: " + similarity.getSimilarity());
                System.out.println("답변: " + similarity.getAnswer());
                System.out.println("-----------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
