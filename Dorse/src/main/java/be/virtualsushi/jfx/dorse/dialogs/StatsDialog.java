package be.virtualsushi.jfx.dorse.dialogs;

import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.control.calendar.DatePicker;
import be.virtualsushi.jfx.dorse.events.CancelStatsEvent;
import be.virtualsushi.jfx.dorse.events.StatsEvent;
import be.virtualsushi.jfx.dorse.fxml.UiComponentBean;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.StatsDTO;
import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class StatsDialog extends UiComponentBean {

	private EventBus eventBus;
	@FXML
	private DatePicker fromDateField;
	@FXML
	private DatePicker toDateField;
	@FXML
	private TextField idField;

	public EventBus getEventBus() {
		return eventBus;
	}

	@Autowired
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@FXML
	public void handleCancel(ActionEvent event) {
		getEventBus().post(new CancelStatsEvent(this.getClass()));
	}

	@FXML
	@SuppressWarnings("unchecked")
	public void handleStats(ActionEvent event) {
		postStatsEvent(new StatsEvent(getStatsEntity()));
	}

	@SuppressWarnings("unchecked")
	protected void postStatsEvent(Object event) {
		getEventBus().post(event);
	}

	protected StatsDTO getStatsEntity() {
		return new StatsDTO(idField.getValue(), fromDateField.getSelectedDate(), toDateField.getSelectedDate());
	}

	@FXML
	protected void handleSave(ActionEvent event) {
	}

	public void onShow(Article article) {
		Date seventy = new Date();
		seventy.setTime(0);
		fromDateField.setSelectedDate(seventy);
		toDateField.setSelectedDate(new Date());
		idField.setValue(""+article.getId());
	}

	@Override
	protected void onUiBinded() {
		super.onUiBinded();
	}
}
