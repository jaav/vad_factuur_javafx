package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.Address;
import be.virtualsushi.jfx.dorse.model.Person;

public class SavePersonEvent extends SaveEntityEvent<Person> {

	public SavePersonEvent(Person entity) {
		super(entity);

	}

}
