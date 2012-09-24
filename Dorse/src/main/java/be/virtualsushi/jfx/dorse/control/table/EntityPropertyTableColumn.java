package be.virtualsushi.jfx.dorse.control.table;

import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import be.virtualsushi.jfx.dorse.model.BaseEntity;

public class EntityPropertyTableColumn<E extends BaseEntity, T> extends TableColumn<E, T> {

	public EntityPropertyTableColumn(String title, Callback<CellDataFeatures<E, T>, ObservableValue<T>> cellFactory) {
		super(title);
		setCellValueFactory(cellFactory);
	}

	public EntityPropertyTableColumn(String title, String propertyName) {
		this(title, new PropertyValueFactory<E, T>(propertyName));
	}

	public EntityPropertyTableColumn(ResourceBundle resources, String propertyName) {
		this(resources.getString(propertyName), propertyName);
	}

}
