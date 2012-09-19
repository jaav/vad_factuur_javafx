package be.virtualsushi.jfx.dorse.control.table;

import java.lang.reflect.Field;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import be.virtualsushi.jfx.dorse.activities.DorseUiActivity;
import be.virtualsushi.jfx.dorse.model.BaseEntity;

import com.zenjava.jfxflow.worker.BackgroundTask;

public class SubitemCellValueFactory<S extends BaseEntity, E extends BaseEntity, T> implements Callback<CellDataFeatures<E, T>, ObservableValue<T>> {

	private final DorseUiActivity<?> activity;
	private SimpleObjectProperty<T> value;
	private Long id;
	private String displayPropertyName;
	private String valuePropertyName;

	private Class<S> valueEntityClass;

	public SubitemCellValueFactory(DorseUiActivity<?> activity, Class<S> valueEntityClass, String valuePropertyName, String displayPropertyName) {
		this.activity = activity;
		value = new SimpleObjectProperty<T>();
		this.valueEntityClass = valueEntityClass;
		this.valuePropertyName = valuePropertyName;
		this.displayPropertyName = displayPropertyName;
	}

	@Override
	public ObservableValue<T> call(CellDataFeatures<E, T> param) {
		if (id == null) {
			try {
				id = extractId(param.getValue());
				new Thread(new BackgroundTask<T>() {

					@Override
					protected T call() throws Exception {
						S entity = activity.getRestApiAccessor().get(id, valueEntityClass);
						return extractValue(entity);
					}

					@Override
					protected void onSuccess(T result) {
						value.set(result);
					}

				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	private T extractValue(S entity) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
		Field field = entity.getClass().getDeclaredField(displayPropertyName);
		field.setAccessible(true);
		return (T) field.get(entity);
	}

	private Long extractId(E entity) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
		Field field = entity.getClass().getDeclaredField(valuePropertyName);
		field.setAccessible(true);
		return (Long) field.get(entity);
	}

}
