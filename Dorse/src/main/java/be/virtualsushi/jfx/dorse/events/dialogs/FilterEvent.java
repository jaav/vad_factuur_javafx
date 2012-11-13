package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.model.OrderLineProperty;

public class FilterEvent<E extends BaseEntity>{

  private final E entity;

	public FilterEvent(E entity) {
		this.entity = entity;
	}

 	public E getEntity() {
 		return entity;
 	}

}
