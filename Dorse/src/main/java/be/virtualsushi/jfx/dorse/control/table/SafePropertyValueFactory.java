package be.virtualsushi.jfx.dorse.control.table;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import be.virtualsushi.jfx.dorse.model.BaseEntity;

public class SafePropertyValueFactory<E extends BaseEntity, T> extends PropertyValueFactory<E, T> {

	public SafePropertyValueFactory(String property) {
		super(property);
	}

	@Override
	public ObservableValue<T> call(CellDataFeatures<E, T> param) {
		return super.call(param);
	}

}
