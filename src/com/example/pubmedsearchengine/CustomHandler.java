package com.example.pubmedsearchengine;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CustomHandler extends DefaultHandler {

    private final String DESCRIPTOR_NAME = "DescriptorName";
    private final String DESCRIPTOR_RECORD = "DescriptorRecord";
    private final String STRING = "String";
    private final String DESCRIPTOR_UI = "DescriptorUI";
    private final String TERM = "Term";
    private final String TERM_LIST = "TermList";
    private final String TREE_NUM = "TreeNumber";

    private HttpSolrServer server;
    private boolean dName = false;
    private boolean dRecord = false;
    private boolean str = false;
    private boolean dUI = false;
    private boolean term = false;
    private boolean termList = false;
    private boolean treeN = false;
    private int counter1 = 0;
    private int counter2 = 0;
    private StringBuffer treeNums = new StringBuffer();
    SolrInputDocument doc;

    public CustomHandler() {
        server = new HttpSolrServer("http://localhost:8983/solr/");
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if (qName.equals(DESCRIPTOR_NAME) && counter1 == 0) {
            counter1 = 1;
            dName = true;
        } else if (qName.equals(DESCRIPTOR_RECORD)) {
            dRecord = true;
            doc = new SolrInputDocument();
        } else if (qName.equals(TERM)) {
            term = true;
        } else if (qName.equals(STRING)) {
            str = true;
        } else if (qName.equals(DESCRIPTOR_UI) && counter2 == 0) {
            dUI = true;
            counter2 = 1;
        } else if (qName.equals(TERM_LIST)) {
            termList = true;
        } else if (qName.equals(TREE_NUM)) {
            treeN = true;
        }

    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals(DESCRIPTOR_NAME)) {
            dName = false;
        } else if (qName.equals(DESCRIPTOR_RECORD)) {
            dRecord = false;
            try {
                doc.addField("id", treeNums.toString());
                treeNums.setLength(0);

           //     System.out.println("Item: " + doc.getField("id") + " "
             //           + doc.getField("name"));// + " " +
                                                // doc.getField("manu"));
                server.add(doc);
                server.commit();

            } catch (SolrServerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            counter1 = 0;
            counter2 = 0;
        } else if (qName.equals(TERM)) {
            term = false;
        } else if (qName.equals(STRING)) {
            str = false;
        } else if (qName.equals(DESCRIPTOR_UI)) {
            dUI = false;
        } else if (qName.equals(TREE_NUM)) {
            treeN = false;
        }
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {

        if (dRecord) {
            if (dName) {
                doc.addField("name", new String(ch, start, length));
            } else if (treeN) {
                // doc.addField("id", new String(ch, start, length));
                if (treeNums.length() > 0)
                    treeNums.append(", " + new String(ch, start, length));
                else
                    treeNums.append(new String(ch, start, length));

            }
            doc.addField("cat", "descriptorRecord");
        }

    }
}
