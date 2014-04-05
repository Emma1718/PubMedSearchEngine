package com.example.pubmedsearchengine;

import gov.nih.nlm.ncbi.www.soap.eutils.*;

import javax.servlet.annotation.WebServlet;

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
		final VerticalLayout layout = new VerticalLayout();
		final Label results = new Label("");
		layout.setMargin(true);
		setContent(layout);

		Button button = new Button("Clic !");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
			    try
		                {
		                    EUtilsServiceStub service = new EUtilsServiceStub();
		                    // call NCBI eGQuery utility
		                    EUtilsServiceStub.EGqueryRequest req = new EUtilsServiceStub.EGqueryRequest();
		                    req.setTerm("mouse");
		                    EUtilsServiceStub.Result res = service.run_eGquery(req);
		                    // results output
		                    result2 = result2 +  "Search term: " + res.getTerm() + "\n";
		                    result2 += "Results: \n";
		                    for (int i = 0; i < res.getEGQueryResult().getResultItem().length; i++)
		                    {
		                        result2 = result2 + "  " + res.getEGQueryResult().getResultItem()[i].getDbName() +
		                                           ": " + res.getEGQueryResult().getResultItem()[i].getCount() + "\n";
		                    }
		                    results.setValue(result2);
		                }
		                catch (Exception e) { System.out.println(e.toString()); }
			}
		});
		layout.addComponent(button);
		
		
		
	}

}