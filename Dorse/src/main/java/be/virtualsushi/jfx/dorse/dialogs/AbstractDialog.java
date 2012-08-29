package be.virtualsushi.jfx.dorse.dialogs;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.fxml.UiComponentBean;

import com.google.common.eventbus.EventBus;

public abstract class AbstractDialog extends UiComponentBean {

	private EventBus eventBus;

	public EventBus getEventBus() {
		return eventBus;
	}

	@Autowired
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

}
