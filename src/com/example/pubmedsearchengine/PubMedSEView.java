package com.example.pubmedsearchengine;

import java.util.ArrayList;
import java.util.Collection;

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
        HorizontalLayout hLabel = new HorizontalLayout();
        setCompositionRoot(vl);
        vl.setSpacing(true);
        vl.setMargin(true);
        hl.setMargin(true);
      //  hLabel.setMargin(true);
        
        initFields();
      
        hl.addComponent(searchTF);
        hl.addComponent(searchBtn);
        
        Label title = new Label("<font size = '20'>PubMed Search Engine</font>");
        title.setContentMode(Label.CONTENT_XHTML);
        title.setSizeUndefined();
      //  title.setWidth("100%");
       // title.setHeight("100%");
        title.setStyleName("title");
        hLabel.setHeight("80pt");
        hLabel.addComponent(title);
     

        hLabel.setComponentAlignment(title, Alignment.MIDDLE_CENTER);


        vl.addComponent(hLabel);
        vl.addComponent(hl);
        vl.addComponent(results);
        vl.setWidth("100%");
        hl.setWidth("100%");
      
        
        
        searchBtn.addClickListener(new ClickListener() {
            
            @Override
            public void buttonClick(ClickEvent event) {
                presenter.search();
            }
        });
    }
    
    private void initFields() {
        searchBtn = new Button("Search");
        searchTF = new TextField();
        Collection<String> terms = new ArrayList<String>();
        for(int i = 0; i < 200; i++)
            terms.add(String.valueOf(i));
        results = new PagingComponent<String>(20, terms, new PagingComponentListener<String>() {

            @Override
            public void displayPage(ChangePageEvent event) {
                
            }
        });
        
        searchTF.setWidth("100%");
        searchTF.setHeight("100%");
        searchBtn.setWidth("20%");
        

    }
}
