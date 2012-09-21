package be.virtualsushi.jfx.dorse.control.table;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import be.virtualsushi.jfx.dorse.model.BaseEntity;

public abstract class EntityPropertyValueFactory<E extends BaseEntity, V> implements Callback<CellDataFeatures<E, V>, ObservableValue<V>> {

	@Override
	public ObservableValue<V> call(CellDataFeatures<E, V> param) {
		ObjectProperty<V> result = new SimpleObjectProperty<V>();
		setPropertyValue(result, param.getValue());
		return result;
	}

	protected abstract void setPropertyValue(ObjectProperty<V> property, E value);

}
