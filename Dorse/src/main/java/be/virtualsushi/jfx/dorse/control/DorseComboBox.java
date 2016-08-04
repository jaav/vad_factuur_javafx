package be.virtualsushi.jfx.dorse.control;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import be.virtualsushi.jfx.dorse.model.Listable;

public class DorseComboBox<T extends Listable> extends ComboBox<T> {
	private final DorseComboBox<T> fcbo = this;
	private ObservableList<T> items;
	private ObservableList<T> filter;
	private String s;
	private Object selection;


	public DorseComboBox() {
		super();
		this.items = getItems();
		this.filter = FXCollections.observableArrayList();

		this.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {

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

		this.setOnKeyReleased(new KeyHandler());

		this.focusedProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				if (newValue == false) {
					s = "";
					fcbo.setItems(items);
					fcbo.getSelectionModel().select((T) selection);
				}

			}
		});

		this.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				if (newValue != null) {
					selection = (Object) newValue;
				}

			}
		});
	}

	public List<T> getAcceptableValues() {
		return getItems();
	}

	public void setAcceptableValues(List<T> acceptableValues) {
		getItems().clear();
		getItems().addAll(acceptableValues);
	}

	private class KeyHandler implements EventHandler<KeyEvent> {

		private SingleSelectionModel<T> sm;

		public KeyHandler() {
			sm = getSelectionModel();
			s = "";
		}

		@Override
		public void handle(KeyEvent event) {
			int index = sm.getSelectedIndex();
			System.out.println("s.length() = " + s.length());
			System.out.println("s = " + s);
				String text = event.getText();
				if (event.getCode() == KeyCode.BACK_SPACE && s.length() > 0) {
					s = s.substring(0, s.length() - 1);
					filter.clear();
				} else if(text.length()==1 && (int)text.charAt(0)>31 && (int)text.charAt(0)<127) {
					String c = event.getCharacter();
					String t = event.getText();
					filter.clear();
					s += event.getText();
				}
				else return;

				if (s.length() == 0) {
					fcbo.setItems(items);
					sm.selectFirst();
					return;
				}
				for (T item : items) {
					if (item.getPrintName().toUpperCase().startsWith(s.toUpperCase())) {

						filter.add(item);

					}
				}
				fcbo.setItems(filter);
				if(event.getText().length()>0 || index<0)
					sm.selectFirst();
				else
					sm.selectFirst();

		}
	}
}