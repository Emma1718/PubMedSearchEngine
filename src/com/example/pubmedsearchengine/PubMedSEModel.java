package com.example.pubmedsearchengine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis2.AxisFault;
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

import gov.nih.nlm.ncbi.www.soap.eutils.EFetchGeneServiceStub.IdListType;
import gov.nih.nlm.ncbi.www.soap.eutils.EFetchPubmedServiceStub;
import gov.nih.nlm.ncbi.www.soap.eutils.EFetchPubmedServiceStub.PubmedArticleType;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceStub;
import gov.nih.nlm.ncbi.www.soap.eutils.EFetchPubmedServiceCallbackHandler;
import gov.nih.nlm.ncbi.www.soap.eutils.EFetchPubmedServiceStub;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceCallbackHandler;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceStub;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class PubMedSEModel {

    String STOPWORDS_PATH = "C:\\Users\\Paulina\\workspace\\PubMedSearchEngine\\src\\files\\stopwords.txt";
    private CharArraySet stopwordsSet = null;
    private EUtilsServiceStub service = null;
    private EFetchPubmedServiceStub service2 = null;

    /*
     * Main method of model
     */
    public List<PubMedDoc> search(String searchText) throws IOException {
        ArrayList<String> str = tokenizeStopStem(searchText);
        ArrayList<String> foundInMesh = new ArrayList<String>();
        for (String s : str) {
            foundInMesh.addAll(searchInMesh(s));
        }
        str.addAll(foundInMesh);
        List<PubMedDoc> result = searchInPubMed(str);
        return result;
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

            for (int i = 0; i < results.size(); ++i) {
                result.add((String) results.get(i).getFieldValue("name"));
            }
        } catch (SolrServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    private void initServices() throws AxisFault {
        service = new EUtilsServiceStub();
        service2 = new EFetchPubmedServiceStub();
    }

    private List<PubMedDoc> searchInPubMed(List<String> searchList)
            throws AxisFault {

        List<PubMedDoc> resultList = new ArrayList<PubMedDoc>();
        if (service == null && service2 == null) {
            initServices();
        }

        EUtilsServiceStub.ESearchRequest req = new EUtilsServiceStub.ESearchRequest();

        for (String s : searchList) {
            try {
                String query = createQuery(s);
                req.setTerm(query);
                req.setUsehistory("y");// important!
                req.setDb("pubmed");
                EUtilsServiceStub.ESearchResult res = service.run_eSearch(req);
                int count = new Integer(res.getCount());

                // results output

                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < res.getIdList().getId().length; i++) {
                    sb.append(res.getIdList().getId()[i]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);

                EUtilsServiceStub.ESummaryRequest req2 = new EUtilsServiceStub.ESummaryRequest();
                String q = sb.toString().replace(" ", ",");
                req2.setId(q);
                req2.setDb("pubmed");
                EUtilsServiceStub.ESummaryResult res2 = service
                        .run_eSummary(req2);

                for (int i = 0; i < res2.getDocSum().length; i++) {
                    String title = res2.getDocSum()[i].getItem()[5]
                            .getItemContent();
                    String id = res2.getDocSum()[i].getId();
                    
                    resultList.add(new PubMedDoc(title, id));
                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        return resultList;
    }

    private String createQuery(String str) {
        String res = str.replace(" ", "+");
        return res;
    }

    public class PubMedDoc {
        String title;
        String link;
        private final String BASE_LINK = "http://www.ncbi.nlm.nih.gov/pubmed/";

        public PubMedDoc(String title, String id) {
            this.title = title;
            link =  BASE_LINK + id;
        }

        public String getTitle() {
            return title;

        }

        public String getLink() {
            return link;
        }
    }
}
