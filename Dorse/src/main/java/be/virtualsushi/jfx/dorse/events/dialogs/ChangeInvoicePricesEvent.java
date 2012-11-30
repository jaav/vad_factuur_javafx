package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.Invoice;
import be.virtualsushi.jfx.dorse.model.OrderLineProperty;

public class ChangeInvoicePricesEvent extends SaveEntityEvent<Invoice> {

	public ChangeInvoicePricesEvent(Invoice entity) {
		super(entity);
	}

}
