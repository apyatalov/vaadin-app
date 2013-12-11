package com;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;
import java.util.Map;

@Theme("mytheme")
public class VaadinApp extends UI {

    @WebServlet(value = "/vui/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = VaadinApp.class, widgetset = "com.AppWidgetSet") //com.vaadin.DefaultWidgetSet
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
//
//        Button button = new Button("Click Me");
//        button.addClickListener(new Button.ClickListener() {
//            public void buttonClick(Button.ClickEvent event) {
//                layout.addComponent(new Label("Thank you for clicking"));
//            }
//        });
//        layout.addComponent(IMG_9271button);

        HorizontalLayout layout1 = new HorizontalLayout();
        layout1.setMargin(new MarginInfo(4));
        Link link = new Link("Home", new ExternalResource("home"));
        Label label = new Label(" >");
        label.setContentMode(ContentMode.PREFORMATTED);
        layout1.addComponent(link);
        layout1.addComponent(label);
        layout.addComponent(layout1);

        TabSheet tabsheet = new TabSheet();
        layout.addComponent(tabsheet);

// Create the first tab
        VerticalLayout tab1 = new VerticalLayout();
        tab1.addComponent(new Embedded(null, new ExternalResource("http://www.automodels.org/photos/Bentley-wallpapers.jpg")));
        tabsheet.addTab(tab1, "Invoices");

// This tab gets its caption from the component caption
        VerticalLayout tab2 = new VerticalLayout();
        tabsheet.addTab(tab2, "Reports");

        CssLayout cssLayout = new CssLayout();
        tab2.addComponent(cssLayout);
        cssLayout.setStyleName("toolbar");
        Button create = new Button("Create");
        create.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Thank You!");
            }
        });
        create.setStyleName("testing");
        cssLayout.setStyleName("margin_top_bottom");
        cssLayout.addComponent(create);
        cssLayout.addComponent(new Button("Edit"));
        cssLayout.addComponent(new Button("Remove"));

        // TODO take a look into http://demo.vaadin.com/book-examples/book/#component.table.generatedcolumns.extended
        final Table table = new Table();
        table.setWidth("800px");
        table.addColumnResizeListener(new Table.ColumnResizeListener(){
            public void columnResize(Table.ColumnResizeEvent event) {
                // Get the new width of the resized column
                int width = event.getCurrentWidth();
                // Get the property ID of the resized column
                String column = (String) event.getPropertyId();
                // Do something with the information
                table.setColumnFooter(column, String.valueOf(width) + "px");
            }
        });

// Must be immediate to send the resize events immediately
        table.setImmediate(true);
        table.setStyleName("margin");

/* Add a few items in the table. */
//        table.addItem(new Object[] {new CheckBox(), "Bla bla report", "Description 1"}, 1);
//        table.addItem(new Object[] {new CheckBox(), "Tra tra report", "Description 2"}, 2);


        class NameColumnGenerator implements Table.ColumnGenerator {
            private final String[] values = {"Bla bla report", "Tra tra report"};
            /**
             * Generates the cell containing the Date value. The column is
             * irrelevant in this use case.
             */
            public String generateCell(Table source, Object itemId, Object columnId) {
                Property prop = source.getItem(itemId).getItemProperty(columnId);
                BeanItem<?> item = (BeanItem<?>) source.getItem(itemId);
                Integer index = (Integer) itemId;
                return values[index - 1];
            }
        }

        class DescColumnGenerator implements Table.ColumnGenerator {
            private final String[] values = {"Description 1", "Description 2"};
            /**
             * Generates the cell containing the Date value. The column is
             * irrelevant in this use case.
             */
            public String generateCell(Table source, Object itemId, Object columnId) {
                Property prop = source.getItem(itemId).getItemProperty(columnId);
                Integer index = (Integer) itemId;
                return values[index - 1];
            }
        }

//        table.addContainerProperty("",              CheckBox.class, null);
        table.addContainerProperty("Name",          String.class, null);
        table.addContainerProperty("Description",   String.class, null);
        table.addGeneratedColumn("Name",    new NameColumnGenerator());
        table.addGeneratedColumn("Description", new DescColumnGenerator());

        BeanItemContainer<Integer> data = new BeanItemContainer<Integer>(Integer.class);
        table.setContainerDataSource(data);
        data.addBean(1);
        data.addBean(2);

        tab2.addComponent(table);
    }
}
