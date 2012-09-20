package be.virtualsushi.jfx.dorse.activities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

public abstract class AbstractEditActivity<N extends Node, E extends BaseEntity> extends AbstractManageEntityActivity<N, E> {

	private Validator validator;

	private class SaveTaskCreator implements TaskCreator<DorseBackgroundTask<E>> {

		private final E entity;

		public SaveTaskCreator(E entityToSave) {
			this.entity = entityToSave;
		}

		@Override
		public DorseBackgroundTask<E> createTask() {
			return new DorseBackgroundTask<E>(this, entity) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@Override
				protected E call() throws Exception {
					getRestApiAccessor().save(entity);
					return entity;
				}

				@Override
				protected void onSuccess(E value) {
					hideLoadingMask();
					goTo(getViewActivityName(), ENTITY_ID_PARAMETER, value.getId());
				}
			};
		}

	}

	@FXML
	public void handleCancel(ActionEvent event) {
		getActivityNavigator().goBack();
	}

	@FXML
	public void handleSave(ActionEvent event) {
		doInBackground(new SaveTaskCreator(getEditedEntity()));
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

	protected abstract AppActivitiesNames getViewActivityName();

}
