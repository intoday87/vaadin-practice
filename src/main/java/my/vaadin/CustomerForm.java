package my.vaadin;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;

public class CustomerForm extends CustomerFormDesign {
	private CustomerService service = CustomerService.getInstance();
	private Customer customer;
	private MyUI myUI;
	private Binder<Customer> binder = new Binder<>(Customer.class);

	public CustomerForm(MyUI myUI) {
		this.myUI = myUI;
		status.setItems(CustomerStatus.values());
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		binder.bindInstanceFields(this);
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
		binder.setBean(customer);

		delete.setVisible(customer.isPersisted());
		setVisible(true);
		firstName.selectAll();
	}

	private void delete() {
		service.delete(customer);
		myUI.updateList();
		setVisible(false);
	}

	private void save() {
		service.save(customer);
		myUI.updateList();
		setVisible(false);
	}
}
