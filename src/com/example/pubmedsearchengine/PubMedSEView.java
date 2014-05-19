package com.example.pubmedsearchengine;

import gov.nih.nlm.ncbi.www.soap.eutils.EFetchPubmedServiceStub.PubmedArticleType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.vaadin.pagingcomponent.PagingComponent;
import org.vaadin.pagingcomponent.PagingComponent.ChangePageEvent;
import org.vaadin.pagingcomponent.PagingComponent.PagingComponentListener;

import com.sun.tools.xjc.Language;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


public class PubMedSEView extends CustomComponent {

    PubMedSEPresenter presenter;
    private Button searchBtn;
    private TextField searchTF;

    PagingComponent results;
    
    public PubMedSEView() {
        init();
    }
    public void registerPresenter(PubMedSEPresenter presenter) {
        this.presenter = presenter;
    }
    
    public void init() {
        VerticalLayout vl = new VerticalLayout();
        HorizontalLayout hl = new HorizontalLayout();
        setCompositionRoot(vl);
        vl.setSpacing(true);
        vl.setMargin(true);
        hl.setMargin(true);
        
        initFields();
      
        hl.addComponent(searchTF);
        hl.addComponent(searchBtn);
        
        Label title = new Label("PubMed Search Engine");
        title.setSizeUndefined();
        title.setStyleName("title");
        title.setImmediate(true);
        vl.addComponent(title);

        vl.addComponent(hl);
        vl.addComponent(results);
        vl.setWidth("100%");
        hl.setWidth("100%");
      
        
        
      
    }
    
    private void initFields() {
        searchBtn = new Button("Search");
        searchBtn.addClickListener(new ClickListener() {
            
            @Override
            public void buttonClick(ClickEvent event) {
                  presenter.search();                
            }
        });
        searchTF = new TextField();
        Collection<String> terms = new ArrayList<String>();
        for(int i = 0; i < 200; i++)
            terms.add(String.valueOf(i));
        searchTF.setWidth("100%");
        searchTF.setHeight("100%");
        searchBtn.setWidth("20%");
        
        results = new PagingComponent(20, 5, terms, new PagingComponentListener<String>() {

            @Override
            public void displayPage(ChangePageEvent<String> event) {
                
            }
        });
        

    }
    
    public void setFoundArticles(List<PubmedArticleType> articles) {
        for(PubmedArticleType p: articles) {
            System.out.println(p.getPubmedData());
        }
    }
    public String getSearchText() {
        return searchTF.getValue();
    }
}
