package be.virtualsushi.jfx.dorse.dialogs;

import java.lang.reflect.ParameterizedType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.IdNamePairEntity;

public class IdNamePairEditDialog<E extends IdNamePairEntity> extends AbstractDialog {

	@FXML
	public TextField nameField;

	@FXML
	@SuppressWarnings("unchecked")
	public void handleSave(ActionEvent event) {
		try {
			E entity = ((Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
			entity.setName(nameField.getValue());
			getEventBus().post(new SaveEntityEvent<E>(entity));
		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		}
	}

}
