package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.OrderLine;
import be.virtualsushi.jfx.dorse.model.OrderLineProperty;
import be.virtualsushi.jfx.dorse.model.OrderLineProperty;

public class SaveOrderLineEvent extends SaveEntityEvent<OrderLineProperty> {

	public SaveOrderLineEvent(OrderLineProperty entity) {
		super(entity);
	}

}
