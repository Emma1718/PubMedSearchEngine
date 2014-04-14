package com.example.pubmedsearchengine;

public class PubMedSEPresenter {

    PubMedSEView view;
    PubMedSEModel model;

    public void setView(PubMedSEView view) {
        this.view = view;
        view.registerPresenter(this);
    }
    
    public void setModel(PubMedSEModel model) {
        this.model = model;
    }
    
    public void search() {
        view.getSearchText();
        model.search();
    }
    
}
