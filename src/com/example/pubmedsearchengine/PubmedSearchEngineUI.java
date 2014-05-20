package com.example.pubmedsearchengine;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ui.UIServerRpc;
import com.vaadin.ui.UI;

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

        setContent(view);
        // model.initMesh();
    }

}