package com.example.demo;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            // Step 1: Parse FAQ JSON
            List<Map<String, String>> faqData = FAQParser.parseFAQ("faq_data_final.json");

            // Step 2: Tokenize FAQ Data
            Map<String, Map<String, Object>> tokenizedData = FAQTokenizer.tokenizeFAQ(faqData);

            // Step 3: Save Tokenized Data
            TokenizedDataSaver.saveTokenizedData("tokenized_faq.json", tokenizedData);

            System.out.println("Tokenization complete. Saved to tokenized_faq.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
