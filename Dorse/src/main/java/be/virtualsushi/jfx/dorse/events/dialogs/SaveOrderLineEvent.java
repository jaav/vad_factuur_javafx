package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.OrderLine;

public class SaveOrderLineEvent extends SaveEntityEvent<OrderLine> {

	public SaveOrderLineEvent(OrderLine entity) {
		super(entity);
	}

}
