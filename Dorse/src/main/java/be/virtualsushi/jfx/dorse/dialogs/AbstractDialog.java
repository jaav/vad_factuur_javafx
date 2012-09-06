package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.events.CancelDialogEvent;
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

	@FXML
	public void handleCancel(ActionEvent event) {
		getEventBus().post(new CancelDialogEvent(this.getClass()));
	}

	public void onShow() {

	}

}
