package be.virtualsushi.jfx.dorse.control;

import java.util.List;

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


	private class KeyHandler implements EventHandler<KeyEvent> {

      private SingleSelectionModel< T > sm;
      private String s;

      public KeyHandler() {
          sm = getSelectionModel();
          s = "";
      }

      @Override
      public void handle( KeyEvent event ) {
          // handle non alphanumeric keys like backspace, delete etc
          if( event.getCode() == KeyCode.BACK_SPACE && s.length()>0)
              s = s.substring( 0, s.length() - 1 );
          else s += event.getText();

          if( s.length() == 0 ) {
              sm.selectFirst();
              return;
          }
          System.out.println( s );
          for( String item: items ) {
              if( item.startsWith( s ) ) sm.select( item );
          }
      }

  }

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
