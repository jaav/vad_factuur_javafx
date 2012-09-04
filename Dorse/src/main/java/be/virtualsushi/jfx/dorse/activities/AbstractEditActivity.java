package be.virtualsushi.jfx.dorse.activities;

import java.lang.reflect.ParameterizedType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.model.BaseEntity;

public abstract class AbstractEditActivity<N extends Node, E extends BaseEntity> extends UiActivity<N> {

	public static final String EDITING_ENTITY_ID_PARAMETER = "editing_entity_id";

	private Validator validator;

	@FXML
	public Label title;

	private E editingEntity;

	@FXML
	public void handleCancel(ActionEvent event) {

	}

	@FXML
	public void handleSave(ActionEvent event) {
		getRestApiAccessor().save(getEditedEntity());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void activated() {
		super.activated();

		Long id = getParameter(EDITING_ENTITY_ID_PARAMETER, Long.class, null);
		if (id != null) {
			editingEntity = getRestApiAccessor().get(id, (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
			title.setText(getResources().getString(getEditTitleKey()));
		} else {
			editingEntity = newEntityInstance();
			title.setText(getResources().getString(getNewTitleKey()));
		}
	}

	protected E getEditingEntity() {
		return editingEntity;
	}

	public Validator getValidator() {
		return validator;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	protected abstract String getEditTitleKey();

	protected abstract String getNewTitleKey();

	protected abstract E newEntityInstance();

	protected abstract void mapFields();

	protected abstract E getEditedEntity();

}
