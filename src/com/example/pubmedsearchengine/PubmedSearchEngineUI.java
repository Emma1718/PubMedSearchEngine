package com.example.pubmedsearchengine;

import java.io.IOException;

import gov.nih.nlm.ncbi.www.soap.eutils.*;

import javax.servlet.annotation.WebServlet;

import org.apache.axis2.AxisFault;
import org.vaadin.objectview.ObjectView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("pubmedsearchengine")
public class PubmedSearchEngineUI extends UI {
    String result2 = "";

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = PubmedSearchEngineUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        
       
        
        PubMedSEView view = new PubMedSEView();
        PubMedSEModel model = new PubMedSEModel();
        PubMedSEPresenter presenter = null;
        try {
            presenter = new PubMedSEPresenter(view, model);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        model = new PubMedSEModel();

        // presenter.setView(view);
        // presenter.setModel(model);
        setContent(view);
    }

}