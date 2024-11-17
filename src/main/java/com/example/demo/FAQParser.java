package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class FAQParser {
    public static List<Map<String, String>> parseFAQ(String fileName) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // ClassLoader를 사용하여 resources 폴더에서 파일 읽기
        InputStream inputStream = FAQParser.class.getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new RuntimeException("Cannot find file in resources folder: " + fileName);
        }

        // JSON 파일을 List<Map<String, String>>으로 파싱
        return mapper.readValue(inputStream, new TypeReference<List<Map<String, String>>>() {});
    }
}