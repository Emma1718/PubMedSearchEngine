package com.example.pubmedsearchengine;

import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceStub;

public class PubMedSEModel {

    public void search() {
        try
        {
           EUtilsServiceStub service = new EUtilsServiceStub();
//            // call NCBI eGQuery utility
//            EUtilsServiceStub.EGqueryRequest req = new EUtilsServiceStub.EGqueryRequest();
//            req.setTerm("mouse");
//            EUtilsServiceStub.Result res = service.run_eGquery(req);
//            // results output
//            result2 = result2 +  "Search term: " + res.getTerm() + "\n";
//            result2 += "Results: \n";
//            for (int i = 0; i < res.getEGQueryResult().getResultItem().length; i++)
//            {
//                result2 = result2 + "  " + res.getEGQueryResult().getResultItem()[i].getDbName() +
//                                   ": " + res.getEGQueryResult().getResultItem()[i].getCount() + "\n";
//            }
//            results.setValue(result2);
        }
        catch (Exception e) { System.out.println(e.toString()); }
    }
}
