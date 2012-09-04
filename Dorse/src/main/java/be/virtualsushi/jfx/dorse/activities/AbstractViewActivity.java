package be.virtualsushi.jfx.dorse.activities;

import java.lang.reflect.ParameterizedType;

import javafx.scene.Node;
import be.virtualsushi.jfx.dorse.model.BaseEntity;

import com.zenjava.jfxflow.worker.BackgroundTask;

public abstract class AbstractViewActivity<N extends Node, E extends BaseEntity> extends UiActivity<N> {

	public static final String VIEWING_ENTITY_ID_PARAMETER = "viewing_entity_id";

	public E viewingEntity;

	@Override
	public void activated() {
		super.activated();

		final Long id = getParameter(VIEWING_ENTITY_ID_PARAMETER, Long.class, null);
		if (id == null) {
			throw new IllegalStateException("Nothing to display. Did you forget to pass " + VIEWING_ENTITY_ID_PARAMETER + " parameter");
		} else {
			// TODO add loading mask
			executeTask(new BackgroundTask<E>() {

				@SuppressWarnings("unchecked")
				@Override
				protected E call() throws Exception {
					return getRestApiAccessor().get(id, (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
				}

				@Override
				protected void onSuccess(E value) {
					viewingEntity = value;
					mapFields();
					// TODO remove loading mask.
				}
			});
		}
	}

	public E getViewingEntity() {
		return viewingEntity;
	}

	protected abstract void mapFields();
}
