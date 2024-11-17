package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

public class TokenizedDataSaver {
    public static void saveTokenizedData(String filePath, Map<String, Map<String, Object>> data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
    }
}
