package be.virtualsushi.jfx.dorse.control;

import java.util.List;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import be.virtualsushi.jfx.dorse.model.Listable;

public class DorseComboBox<T extends Listable> extends ComboBox<T> {

	public DorseComboBox() {
		super();
		setCellFactory(new Callback<ListView<T>, ListCell<T>>() {

			@Override
			public ListCell<T> call(ListView<T> param) {
				return new ListCell<T>() {

					@Override
					protected void updateItem(T item, boolean empty) {
						super.updateItem(item, empty);
						if (!empty) {
							setText(item.getPrintName());
						}
					}

				};
			}
		});
	}

	public void setAcceptableValues(List<T> acceptableValues) {
		getItems().clear();
		getItems().addAll(acceptableValues);
	}

	public List<T> getAcceptableValues() {
		return getItems();
	}

}
