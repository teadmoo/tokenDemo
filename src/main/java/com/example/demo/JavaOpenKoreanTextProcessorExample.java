package com.example.demo;

import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import scala.collection.Seq;

import java.util.List;
import java.util.stream.Collectors;

public class JavaOpenKoreanTextProcessorExample {
    public static void main(String[] args) {
        String text =
                "인증서 갱신은 유효기한이 만료되는 인증서에 대하여 기한 만료일 90일전부터 유효기한 만료일까지 가능하며, 갱신하고자 하는 금융인증서를 제출할 수 있어야 합니다.당행에서 발급받은 금융결제원 금융인증서만 갱신가능하며, 타기관 또는 타은행에서 발급받은 금융인증서는 해당 은행이나 발급기관을 이용하셔야 합니다.갱신한 인증서는 현 인증서의 잔여기간 포함하여 유효기한 만료일로부터 3년간 유효합니다. 이 부분을 참고하시어 갱신하시면 정상 이용이 가능합니다";

        // Normalize
        CharSequence normalized = OpenKoreanTextProcessorJava.normalize(text);
        System.out.println("Normalized Text: " + normalized);

        // Tokenize
        Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);

        // Convert tokens to List<KoreanToken>
        List<KoreanTokenJava> tokenList = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);

        // Extract Nouns
        List<String> nouns = tokenList.stream()
                .filter(token -> token.getPos().toString().equals("Noun")) // POS가 Noun인 것만 필터링
                .map(KoreanTokenJava::getText) // 텍스트 추출
                .toList();

        System.out.println("Extracted Nouns: " + nouns);
        // Output: Extracted Nouns: [한국어, 처리, 예시]
    }
}
