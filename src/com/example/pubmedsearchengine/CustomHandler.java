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
    private HttpSolrServer server;
    private boolean name = false;
    private int counter = 0;

    public CustomHandler() {
         server = new HttpSolrServer("http://localhost:8983/solr/");
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase(DESCRIPTOR_NAME)) {
            name = true;
            counter++;
        }

    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        name = false;

    }

    public void characters(char ch[], int start, int length)
            throws SAXException {

        if (name) {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", counter);
            doc.addField("name", new String(ch, start, length));
            doc.addField("cat", "descriptorName");
            try {
               server.add(doc);
            //    server.commit();
            } catch (SolrServerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(new String(ch, start, length));
        }

    }
}
