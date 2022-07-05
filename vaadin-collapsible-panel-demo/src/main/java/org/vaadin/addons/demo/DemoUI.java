package org.vaadin.addons.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.collapsiblepanel.CollapsiblePanel;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("CollapsiblePanel Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.vaadin.addons.demo.DemoWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {

		// Initialize our new UI component
		final Panel panel = new Panel("header");
		panel.setContent(new Label("label"));
		panel.setWidth(50, Unit.PERCENTAGE);
		final CollapsiblePanel collapsiblePanelExtension = new CollapsiblePanel(panel);

		collapsiblePanelExtension.addExpandListener(evt -> System.out.println("Expand"));
		collapsiblePanelExtension.addCollapseListener(evt -> System.out.println("Collapse"));

		// Show it in the middle of the screen
		final VerticalLayout layout = new VerticalLayout();
		layout.setStyleName("demoContentLayout");
		layout.setSizeFull();
		layout.addComponent(panel);
		layout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		setContent(layout);
	}
}
