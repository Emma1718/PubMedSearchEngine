package com.example.pubmedsearchengine;


import java.io.IOException;
import java.io.InputStream;

import org.apache.lucene.analysis.PorterStemFilter;
import org.xml.sax.InputSource;

import com.aliasi.lingmed.mesh.MeshParser;
import com.aliasi.tokenizer.PorterStemmerTokenizerFactory;

import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceStub;

public class PubMedSEModel {

    public void search() {
        try
        {
            EUtilsServiceStub service = new EUtilsServiceStub();
            // call NCBI ESearch utility
            // NOTE: search term should be URL encoded
            EUtilsServiceStub.ESearchRequest req = new EUtilsServiceStub.ESearchRequest();
            req.setDb("pmc");
            req.setTerm("stem+cells+AND+free+fulltext[filter]");
            req.setRetMax("15");
            EUtilsServiceStub.ESearchResult res = service.run_eSearch(req);
            // results output
            System.out.println("Original query: stem cells AND free fulltext[filter]");
            System.out.println("Found ids: " + res.getCount());
            System.out.print("First " + res.getRetMax() + " ids: ");
            for (int i = 0; i < res.getIdList().getId().length; i++)
            {
                System.out.print(res.getIdList().getId()[i] + " ");
            }
            System.out.println();
        }
        catch (Exception e) { System.out.println(e.toString()); }
        
       
    }
}
