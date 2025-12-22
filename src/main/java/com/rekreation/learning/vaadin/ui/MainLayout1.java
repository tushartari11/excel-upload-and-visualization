package com.rekreation.learning.vaadin.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("main1")
@RolesAllowed("ADMIN")
public class MainLayout1 extends VerticalLayout {

    private Div leftPanel;
    private Div rightPanel;
    private Button activeLeftButton = null;
    private Button activeRightButton = null;

    public MainLayout1() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Header
        add(createHeader());

        // Main container with side panels and content
        HorizontalLayout mainContainer = createMainContainer();
        mainContainer.setSizeFull();
        add(mainContainer);
        setFlexGrow(1, mainContainer);

        // Footer
        add(createFooter());
    }

    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(false);
        header.setSpacing(false);
        header.getStyle()
                .set("background", "#2c3e50")
                .set("color", "white")
                .set("box-shadow", "0 2px 5px rgba(0,0,0,0.1)");

        // Logo
        HorizontalLayout logo = new HorizontalLayout();
        logo.setPadding(true);
        logo.setSpacing(true);
        logo.getStyle()
                .set("background", "#1a252f")
                .set("padding", "15px 30px");

        Div logoIcon = new Div();
        logoIcon.setText("C");
        logoIcon.getStyle()
                .set("width", "40px")
                .set("height", "40px")
                .set("background", "#3498db")
                .set("border-radius", "5px")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("font-size", "20px")
                .set("font-weight", "bold");

        Span companyName = new Span("Company");
        companyName.getStyle().set("font-size", "24px").set("font-weight", "bold");

        logo.add(logoIcon, companyName);

        // Tabs menu
        Tabs tabs = new Tabs();
        tabs.getStyle()
                .set("flex", "1")
                .set("margin-left", "20px");

        Tab homeTab = createTab("Home", true);
        Tab productsTab = createTab("Products", false);
        Tab servicesTab = createTab("Services", false);
        Tab aboutTab = createTab("About", false);
        Tab contactTab = createTab("Contact", false);

        tabs.add(homeTab, productsTab, servicesTab, aboutTab, contactTab);

        header.add(logo, tabs);
        return header;
    }

    private Tab createTab(String label, boolean selected) {
        Tab tab = new Tab(label);
        tab.getStyle()
                .set("color", "white")
                .set("padding", "20px 30px");

        if (selected) {
            tab.getStyle().set("border-bottom", "3px solid #3498db");
        }

        return tab;
    }

    private HorizontalLayout createMainContainer() {
        HorizontalLayout container = new HorizontalLayout();
        container.setSizeFull();
        container.setPadding(false);
        container.setSpacing(false);

        // Left navigation bar
        VerticalLayout leftNav = createLeftNavBar();

        // Left panel (initially hidden)
        leftPanel = createSidePanel("left");

        // Content area
        Div contentArea = createContentArea();

        // Right panel (initially hidden)
        rightPanel = createSidePanel("right");

        // Right navigation bar
        VerticalLayout rightNav = createRightNavBar();

        container.add(leftNav, leftPanel, contentArea, rightPanel, rightNav);
        container.setFlexGrow(1, contentArea);

        return container;
    }

    private VerticalLayout createLeftNavBar() {
        VerticalLayout nav = new VerticalLayout();
        nav.setSpacing(false);
        nav.setPadding(false);
        nav.setWidth("50px");
        nav.setMinWidth("50px");
        nav.getStyle()
                .set("background", "#f8f9fa")
                .set("border-right", "1px solid #e0e0e0")
                .set("padding", "8px 0")
                .set("flex-shrink", "0");

        // Icons for left navigation
        VaadinIcon[] icons = {VaadinIcon.SEARCH, VaadinIcon.VIEWPORT, VaadinIcon.SITEMAP, VaadinIcon.LOCATION_ARROW};

        for (int i = 1; i <= 4; i++) {
            final int option = i;
            Icon icon = icons[i - 1].create();
            icon.getStyle().set("color", "#2c3e50");

            Button btn = new Button(icon);
            btn.getStyle()
                    .set("background", "transparent")
                    .set("border", "none")
                    .set("min-width", "40px")
                    .set("min-height", "40px")
                    .set("margin", "4px auto")
                    .set("cursor", "pointer");
            btn.addClickListener(e -> toggleLeftPanel(option, btn));
            nav.add(btn);
        }

        return nav;
    }

    private VerticalLayout createRightNavBar() {
        VerticalLayout nav = new VerticalLayout();
        nav.setSpacing(false);
        nav.setPadding(false);
        nav.setWidth("50px");
        nav.setMinWidth("50px");
        nav.getStyle()
                .set("background", "#f8f9fa")
                .set("border-left", "1px solid #e0e0e0")
                .set("padding", "8px 0")
                .set("flex-shrink", "0");

        // Icons for right navigation
        VaadinIcon[] icons = {VaadinIcon.FILM, VaadinIcon.CALENDAR, VaadinIcon.SITEMAP, VaadinIcon.HOME};

        for (int i = 1; i <= 4; i++) {
            final int option = i;
            Icon icon = icons[i - 1].create();
            icon.getStyle().set("color", "#2c3e50");

            Button btn = new Button(icon);
            btn.getStyle()
                    .set("background", "transparent")
                    .set("border", "none")
                    .set("min-width", "40px")
                    .set("min-height", "40px")
                    .set("margin", "4px auto")
                    .set("cursor", "pointer");
            btn.addClickListener(e -> toggleRightPanel(option, btn));
            nav.add(btn);
        }

        return nav;
    }

    private Div createSidePanel(String side) {
        Div panel = new Div();
        panel.setWidth("250px");
        panel.getStyle()
                .set("background", "#ecf0f1")
                .set("padding", "20px")
                .set("border-" + (side.equals("left") ? "right" : "left"), "2px solid #bdc3c7")
                .set("overflow-y", "auto")
                .set("display", "none")
                .set("flex-shrink", "0");

        H3 title = new H3(side.equals("left") ? "Left Panel" : "Right Panel");
        Paragraph text = new Paragraph("Click a button to see content here.");

        panel.add(title, text);
        return panel;
    }

    private Div createContentArea() {
        Div content = new Div();
        content.getStyle()
                .set("background", "white")
                .set("padding", "40px")
                .set("overflow-y", "auto");

        H2 title = new H2("Main Content Area");
        Paragraph p1 = new Paragraph("This is your main content area. Click the buttons on the left or right to open side panels.");
        Paragraph p2 = new Paragraph("The side panels will slide in and take 250px of width.");
        Paragraph p3 = new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");

        content.add(title, p1, p2, p3);
        return content;
    }

    private Component createFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.CENTER);
        footer.setSpacing(false);
        footer.setPadding(false);
        footer.getStyle()
                .set("background", "#f8f9fa")
                .set("border-top", "1px solid #e0e0e0")
                .set("padding", "8px 0");

        // Footer icons: +, -, save, undo, redo, download
        VaadinIcon[] icons = {
                VaadinIcon.PLUS,
                VaadinIcon.MINUS,
                VaadinIcon.CHECK,      // Save
                VaadinIcon.ARROW_BACKWARD,  // Undo
                VaadinIcon.ARROW_FORWARD,   // Redo
                VaadinIcon.DOWNLOAD
        };

        String[] tooltips = {"Add", "Remove", "Save", "Undo", "Redo", "Export"};

        for (int i = 0; i < icons.length; i++) {
            Icon icon = icons[i].create();
            icon.getStyle().set("color", "#2c3e50");

            Button btn = new Button(icon);
            btn.getStyle()
                    .set("background", "transparent")
                    .set("border", "none")
                    .set("min-width", "40px")
                    .set("min-height", "40px")
                    .set("margin", "0 8px")
                    .set("cursor", "pointer");
            btn.getElement().setAttribute("title", tooltips[i]);

            footer.add(btn);
        }

        return footer;
    }

    private void toggleLeftPanel(int option, Button btn) {
        if (activeLeftButton == btn) {
            // Close panel - same button clicked again
            leftPanel.getStyle().set("display", "none");
            btn.getStyle().set("background", "transparent");
            activeLeftButton = null;
        } else {
            // Reset previous active button
            if (activeLeftButton != null) {
                activeLeftButton.getStyle().set("background", "transparent");
            }

            // Update panel content
            leftPanel.removeAll();
            H3 title = new H3("Left Panel - Option " + option);
            Paragraph p1 = new Paragraph("This is the content for left option " + option + ".");
            Paragraph p2 = new Paragraph("You can add any content here - forms, links, information, etc.");
            leftPanel.add(title, p1, p2);

            // Show panel and highlight button
            leftPanel.getStyle().set("display", "block");
            btn.getStyle().set("background", "rgba(52, 152, 219, 0.2)");
            activeLeftButton = btn;
        }
    }

    private void toggleRightPanel(int option, Button btn) {
        if (activeRightButton == btn) {
            // Close panel - same button clicked again
            rightPanel.getStyle().set("display", "none");
            btn.getStyle().set("background", "transparent");
            activeRightButton = null;
        } else {
            // Reset previous active button
            if (activeRightButton != null) {
                activeRightButton.getStyle().set("background", "transparent");
            }

            // Update panel content
            rightPanel.removeAll();
            H3 title = new H3("Right Panel - Action " + option);
            Paragraph p1 = new Paragraph("This is the content for right action " + option + ".");
            Paragraph p2 = new Paragraph("You can add any content here - tools, settings, navigation, etc.");
            rightPanel.add(title, p1, p2);

            // Show panel and highlight button
            rightPanel.getStyle().set("display", "block");
            btn.getStyle().set("background", "rgba(52, 152, 219, 0.2)");
            activeRightButton = btn;
        }
    }
}