package be.virtualsushi.jfx.dorse.activities;

import java.lang.reflect.ParameterizedType;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

public abstract class AbstractManageEntityActivity<N extends Node, E extends BaseEntity> extends DorseUiActivity<N> {

	public static final String ENTITY_ID_PARAMETER = "entity_id";

	private class LoadEntityTaskCreator implements TaskCreator<DorseBackgroundTask<E>> {

		private final Object[] parameters;

		public LoadEntityTaskCreator(Object... parameters) {
			this.parameters = parameters;
		}

		@Override
		public DorseBackgroundTask<E> createTask() {
			return new DorseBackgroundTask<E>(this, parameters) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				};

				@SuppressWarnings("unchecked")
				@Override
				protected E call() throws Exception {
					E result = null;
					if (getParameters()[0] != null) {
						result = getRestApiAccessor().get((Long) parameters[0],
								(Class<E>) ((ParameterizedType) AbstractManageEntityActivity.this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
					} else {
						result = newEntityInstance();
					}
					doCustomBackgroundInitialization(result);
					return result;
				}

				@Override
				protected void onSuccess(E value) {
					entity = value;
					mapFields(value);
					hideLoadingMask();
				}

        @Override
        protected void onError(Throwable exception) {
          hideLoadingMask();
        }

			};
		}

	}

	@FXML
	public Label title;

	private E entity;

	@Override
	protected void started() {
		super.started();

		Long id = getParameter(ENTITY_ID_PARAMETER, Long.class, null);
		if (id == null && !canCreateNewEntity()) {
			throw new IllegalStateException("Nothing to display. Did you forget to pass " + ENTITY_ID_PARAMETER + " parameter");
		} else {
			doInBackground(new LoadEntityTaskCreator(id));
		}

	}

	protected E getEntity() {
		return entity;
	}

  protected void setEntity(E entity) {
 		this.entity = entity;
 	}

	protected boolean canCreateNewEntity() {
		return false;
	}

	protected E newEntityInstance() {
		return null;
	}

	protected abstract void doCustomBackgroundInitialization(E entity);

	protected abstract void mapFields(E entity);

}
