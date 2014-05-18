package com.example.pubmedsearchengine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceStub;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class PubMedSEModel {

    String STOPWORDS_PATH = "C:\\Users\\Paulina\\workspace\\PubMedSearchEngine\\src\\files\\stopwords.txt";
    public CharArraySet stopwordsSet = null;

    /*
     * Main method of model
     */
    public void search(String searchText) throws IOException {
        ArrayList<String> str = tokenizeStopStem(searchText);
        System.out.println("Tokenized str: " + str.toString());
        ArrayList<String> foundInMesh = new ArrayList<String>();
        for (String s : str) {
             foundInMesh.addAll(searchInMesh(s));
        }
    }

    /*
     * Method reading English stopwords from file and add them into CharArraySet
     */
    public CharArraySet initStopWordsSet() throws IOException {
        List<String> stopwords = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(
                STOPWORDS_PATH));

        while (reader.ready()) {
            String s = reader.readLine();
            stopwords.add(s);
        }
        reader.close();
        CharArraySet stopwordsSet2 = new CharArraySet(Version.LUCENE_47,
                stopwords, true);
        return stopwordsSet2;
    }

    /*
     * Method which tokenize input, removes stopwords and stem
     */
    private ArrayList<String> tokenizeStopStem(String input) throws IOException {
        if (stopwordsSet == null)
            stopwordsSet = initStopWordsSet();
        ArrayList<String> tokens = new ArrayList<String>();
        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_47,
                new StringReader(input));
        tokenStream = new StopFilter(Version.LUCENE_47, tokenStream,
                stopwordsSet); // removing stopwords
        tokenStream = new PorterStemFilter(tokenStream); // stemming

        CharTermAttribute charTermAttr = tokenStream
                .getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        try {
            while (tokenStream.incrementToken()) {
                tokens.add(charTermAttr.toString());

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tokens;
    }

    private void searchInPubMed() {
        try {
            EUtilsServiceStub service = new EUtilsServiceStub();

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
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void initMesh() {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        DefaultHandler handler = new CustomHandler();

        try {
            saxParser = factory.newSAXParser();
            saxParser.parse("C:\\Users\\Paulina\\mesh.xml", handler);

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Parsing finish");

    }

    private ArrayList<String> searchInMesh(String word) {
        ArrayList<String> result = new ArrayList<String>();
        HttpSolrServer solr = new HttpSolrServer("http://localhost:8983/solr");

        String strQ = "/.*" + word + ".*/";
        SolrQuery query = new SolrQuery();
       // query.se
        query.setQuery(strQ);
        query.addFilterQuery("cat:descriptorRecord");
        query.setFields("id", "name");
        query.setStart(0);
       // query.set("defType", "edismax");

        QueryResponse response;
        try {
            response = solr.query(query);
            SolrDocumentList results = response.getResults();
            System.out.println("query: " + query.toString());

            for (int i = 0; i < results.size(); ++i) {
                System.out.println(results.get(i).getFieldValue("name"));
                result.add((String) results.get(i).getFieldValue("name"));
            }
        } catch (SolrServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

}
