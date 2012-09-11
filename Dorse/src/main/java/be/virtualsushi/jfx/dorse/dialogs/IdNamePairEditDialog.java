package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.IdNamePairEntity;

public abstract class IdNamePairEditDialog<E extends IdNamePairEntity> extends AbstractDialog {

	@FXML
	public TextField nameField;

	@FXML
	public void handleSave(ActionEvent event) {
		E entity = createNewEntityInstance();
		entity.setName(nameField.getValue());
		getEventBus().post(createSaveEvent(entity));
	}

	protected abstract E createNewEntityInstance();

	protected abstract SaveEntityEvent<E> createSaveEvent(E entity);

}
