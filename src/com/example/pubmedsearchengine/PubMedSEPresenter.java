package com.example.pubmedsearchengine;

import java.io.IOException;

public class PubMedSEPresenter {

    PubMedSEView view;
    PubMedSEModel model;

    public PubMedSEPresenter(PubMedSEView view, PubMedSEModel model) throws IOException {
        setView(view);
        setModel(model);
    }
    public void setView(PubMedSEView view) {
        this.view = view;
        view.registerPresenter(this);
    }
    
    public void setModel(PubMedSEModel model) {
        this.model = model;
    }
    
    public void search() {
        try {
            model.search(view.getSearchText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
