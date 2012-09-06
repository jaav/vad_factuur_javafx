package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.model.BaseEntity;

public class SaveEntityEvent<E extends BaseEntity> {

	private final E entity;

	public SaveEntityEvent(E entity) {
		this.entity = entity;
	}

	public E getEntity() {
		return entity;
	}

}
