package com.example.pubmedsearchengine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceStub;

public class PubMedSEModel {

    String STOPWORDS_PATH = "C:\\Users\\Paulina\\workspace\\PubMedSearchEngine\\src\\files\\stopwords.txt";
    public CharArraySet stopwordsSet;
    
    public void search(String searchText) throws IOException {
        String str = tokenizeStopStem(searchText);
        System.out.println(str);
    }

    public CharArraySet initStopWordsSet() throws IOException {
        List<String> stopwords = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(STOPWORDS_PATH));

        while (reader.ready()) {
          String s = reader.readLine();
          stopwords.add(s);
          System.out.println(s);
        }
        reader.close();
        CharArraySet stopwordsSet = new CharArraySet(stopwords, true);
        return stopwordsSet;
    }
    
    private String tokenizeStopStem(String input) throws IOException {
//        if(stopwordsSet.isEmpty())
//            stopwordsSet = initStopWordsSet();
//        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_47, new StringReader(input));
//        tokenStream = new StopFilter(true, tokenStream, stopwordsSet);
//        tokenStream = new PorterStemFilter(tokenStream);
// 
//        StringBuilder sb = new StringBuilder();
//        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
//        CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);
//        try{
//            while (tokenStream.incrementToken()) {
//                if (sb.length() > 0) {
//                    sb.append(" ");
//                }
//                sb.append(charTermAttr.toString());
//            }
//        }
//        catch (IOException e){
//            System.out.println(e.getMessage());
//        }
//        return sb.toString();
        return null;
}
    
    private void searchInPubMed() {
        try {
            EUtilsServiceStub service = new EUtilsServiceStub();
            // call NCBI ESearch utility
            // NOTE: search term should be URL encoded
            EUtilsServiceStub.ESearchRequest req = new EUtilsServiceStub.ESearchRequest();
            req.setDb("pmc");
            req.setTerm("stem+cells+AND+free+fulltext[filter]");
            req.setRetMax("15");
            EUtilsServiceStub.ESearchResult res = service.run_eSearch(req);
            // results output
            System.out
                    .println("Original query: stem cells AND free fulltext[filter]");
            System.out.println("Found ids: " + res.getCount());
            System.out.print("First " + res.getRetMax() + " ids: ");
            for (int i = 0; i < res.getIdList().getId().length; i++) {
                System.out.print(res.getIdList().getId()[i] + " ");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
