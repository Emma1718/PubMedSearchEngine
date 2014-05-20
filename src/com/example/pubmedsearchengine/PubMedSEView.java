package com.example.pubmedsearchengine;

import gov.nih.nlm.ncbi.www.soap.eutils.EFetchPubmedServiceStub.PubmedArticleType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.vaadin.pagingcomponent.ComponentsManager;
import org.vaadin.pagingcomponent.PagingComponent;
import org.vaadin.pagingcomponent.PagingComponent.ChangePageEvent;
import org.vaadin.pagingcomponent.PagingComponent.PagingComponentListener;
import org.vaadin.pagingcomponent.builder.ElementsBuilder;
import org.vaadin.pagingcomponent.button.ButtonPageNavigator;
import org.vaadin.pagingcomponent.customizer.style.StyleCustomizer;
import org.vaadin.pagingcomponent.listener.impl.SimplePagingComponentListener;

import com.example.pubmedsearchengine.PubMedSEModel.PubMedDoc;
import com.sun.tools.xjc.Language;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PubMedSEView extends CustomComponent {

    private PubMedSEPresenter presenter;
    private Button searchBtn;
    private TextField searchTF;
    private StyleCustomizer styler;
    private VerticalLayout vl;
    private PagingComponent<PubMedDoc> pagingComponent;
    private final static String ACTUAL_RESULT = "actualResult";
    

    public PubMedSEView() {
        init();
    }

    public void registerPresenter(PubMedSEPresenter presenter) {
        this.presenter = presenter;
    }

    public void init() {
        // layout = new VerticalLayout();
        vl = new VerticalLayout();
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
        
        if (VaadinSession.getCurrent().getAttribute(ACTUAL_RESULT) != null) {
            System.out.print("Nie null");
            setFoundArticles((List<PubMedDoc>) VaadinSession.getCurrent().getAttribute(ACTUAL_RESULT));
        }
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
        for (int i = 0; i < 200; i++)
            terms.add(String.valueOf(i));
        searchTF.setWidth("100%");
        searchTF.setHeight("100%");
        searchBtn.setWidth("20%");

        styler = new StyleCustomizer() {

            @Override
            public void styleButtonPageNormal(ButtonPageNavigator button,
                    int pageNumber) {
                button.setPage(pageNumber);
                button.removeStyleName("styleRed");
            }

            @Override
            public void styleButtonPageCurrentPage(ButtonPageNavigator button,
                    int pageNumber) {
                button.setPage(pageNumber, "[" + pageNumber + "]"); 
                button.addStyleName("styleRed");
                button.focus();
            }

            @Override
            public void styleTheOthersElements(ComponentsManager manager,
                    ElementsBuilder builder) {
                // if the number of pages is less than 2, the other buttons are
                // not created.
                if (manager.getNumberTotalOfPages() < 2) {
                    return;
                }

                // Allow to hide these buttons when the first page is selected
                boolean visible = !manager.isFirstPage();
                builder.getButtonFirst().setVisible(visible);
                builder.getButtonPrevious().setVisible(visible);
                builder.getFirstSeparator().setVisible(visible);

                // Allow to hide these buttons when the last page is selected
                visible = !manager.isLastPage();
                builder.getButtonLast().setVisible(visible);
                builder.getButtonNext().setVisible(visible);
                builder.getLastSeparator().setVisible(visible);
            }

        };

    }

    public void setFoundArticles(List<PubMedDoc> articles) {
        final VerticalLayout mainLayout = new VerticalLayout();
        final VerticalLayout itemsArea = new VerticalLayout();
        pagingComponent = new PagingComponent<PubMedDoc>(30, 10, articles,
                styler,
                new SimplePagingComponentListener<PubMedDoc>(itemsArea) {

                    @Override
                    protected Component displayItem(int index, PubMedDoc item) {
                        // This method allows to create a Component to display
                        // an item fetched
                        return new Label("<a href = " + item.getLink() + ">"
                                + item.getTitle() + "</a>", ContentMode.HTML);
                    }

                });
        mainLayout.addComponent(itemsArea);
        mainLayout.addComponent(pagingComponent);
       
        VaadinSession.getCurrent().setAttribute(ACTUAL_RESULT, articles);
        vl.addComponent(mainLayout);

    }

    public String getSearchText() {
        return searchTF.getValue();
    }
}
