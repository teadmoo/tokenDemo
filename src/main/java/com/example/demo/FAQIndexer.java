//package com.example.demo;
//
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.ko.KoreanAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.TextField;
//import org.apache.lucene.index.DirectoryWriter;
//import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.RAMDirectory;
//
//import java.util.List;
//import java.util.Map;
//
//public class FAQIndexer {
//    private final Directory index;
//    private final Analyzer analyzer;
//
//    public FAQIndexer() {
//        this.index = new RAMDirectory(); // 메모리 기반 디렉토리
//        this.analyzer = new KoreanAnalyzer(); // 한국어 분석기
//    }
//
//    public void indexFAQData(List<Map<String, String>> faqData) throws Exception {
//        IndexWriterConfig config = new IndexWriterConfig(analyzer);
//        try (DirectoryWriter writer = new DirectoryWriter(index, config)) {
//            for (Map<String, String> faq : faqData) {
//                Document doc = new Document();
//                doc.add(new TextField("question", faq.get("question"), Field.Store.YES));
//                doc.add(new TextField("answer", faq.get("answer"), Field.Store.YES));
//                writer.addDocument(doc);
//            }
//        }
//    }
//
//    public Directory getIndex() {
//        return index;
//    }
//
//    public Analyzer getAnalyzer() {
//        return analyzer;
//    }
//}
