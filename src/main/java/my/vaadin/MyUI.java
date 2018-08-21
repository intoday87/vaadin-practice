package my.vaadin;

import javax.servlet.annotation.WebServlet;

import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

	private CustomerService service = CustomerService.getInstance();
	private Grid<Customer> grid = new Grid<>(Customer.class);
	private TextField filterText = new TextField();
	private CustomerForm form = new CustomerForm(this);

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		filterText.setPlaceholder("filter by name");
		filterText.addValueChangeListener(e -> updateList(filterText.getValue()));
		filterText.setValueChangeMode(ValueChangeMode.LAZY);

		Button clearFilterButton = new Button(VaadinIcons.CLOSE);
		clearFilterButton.setDescription("Clear the current filter");
		clearFilterButton.addClickListener(e -> filterText.clear());

		CssLayout filtering = new CssLayout();
		filtering.addComponents(filterText, clearFilterButton);
		filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		VerticalLayout layout = new VerticalLayout();

		HorizontalLayout main = new HorizontalLayout();
		main.setSizeFull();
		grid.setSizeFull();
		main.addComponents(grid, form);

		updateList(filterText.getValue());

		grid.setColumns("firstName", "lastName", "email");

		form.setVisible(false);

		grid.asSingleSelect().addValueChangeListener(e -> {
			if (e.getValue() == null) {
				form.setVisible(false);
			} else {
				form.setCustomer(e.getValue());
			}
		});

		Button addCustomerButton = new Button("Add customer");
		addCustomerButton.addClickListener(e -> {
			grid.asSingleSelect().clear();
			form.setCustomer(new Customer());
		});

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addComponents(filtering, addCustomerButton);

		layout.addComponents(toolbar, main);

		setContent(layout);
	}

	public void updateList() {
		List<Customer> customers = service.findAll();
		grid.setItems(customers);
	}

	public void updateList(String search) {
		List<Customer> customers = search == null ? service.findAll() : service.findAll(search);
		grid.setItems(customers);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
