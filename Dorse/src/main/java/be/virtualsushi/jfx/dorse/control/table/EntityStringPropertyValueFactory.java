package be.virtualsushi.jfx.dorse.control.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import be.virtualsushi.jfx.dorse.model.BaseEntity;

public abstract class EntityStringPropertyValueFactory<E extends BaseEntity> implements Callback<CellDataFeatures<E, String>, ObservableValue<String>> {

	@Override
	public ObservableValue<String> call(CellDataFeatures<E, String> param) {
		SimpleStringProperty result = new SimpleStringProperty();
		setPropertyValue(result, param.getValue());
		return result;
	}

	protected abstract void setPropertyValue(SimpleStringProperty property, E value);

}
