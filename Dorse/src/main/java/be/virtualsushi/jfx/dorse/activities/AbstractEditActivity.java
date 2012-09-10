package be.virtualsushi.jfx.dorse.activities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.events.authentication.AuthorizationRequiredEvent;
import be.virtualsushi.jfx.dorse.model.BaseEntity;

public abstract class AbstractEditActivity<N extends Node, E extends BaseEntity> extends AbstractManageEntityActivity<N, E> {

	private Validator validator;

	@FXML
	public Label title;

	@FXML
	public void handleCancel(ActionEvent event) {
		getEventBus().post(new AuthorizationRequiredEvent());
	}

	@FXML
	public void handleSave(ActionEvent event) {
		getRestApiAccessor().save(getEditedEntity());
	}

	@Override
	protected void started() {
		super.started();

		if (getParameter(ENTITY_ID_PARAMETER, Long.class, null) != null) {
			title.setText(getResources().getString(getEditTitleKey()));
		} else {
			title.setText(getResources().getString(getNewTitleKey()));
		}
	}

	public Validator getValidator() {
		return validator;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@Override
	protected boolean canCreateNewEntity() {
		return true;
	}

	protected abstract String getEditTitleKey();

	protected abstract String getNewTitleKey();

	protected abstract E getEditedEntity();

}
