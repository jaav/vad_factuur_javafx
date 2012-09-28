package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.Stock;

public class SaveStockEvent extends SaveEntityEvent<Stock> {

	public SaveStockEvent(Stock entity) {
		super(entity);
	}

}
