package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.control.DeleteButton;
import be.virtualsushi.jfx.dorse.control.DetailsButton;
import be.virtualsushi.jfx.dorse.control.EditButton;
import be.virtualsushi.jfx.dorse.control.table.EntityPropertyTableColumn;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.ResourceBundle;

public abstract class AbstractBrowserActivity extends DorseUiActivity<BorderPane> {

  @Autowired
 	protected ActivityNavigator activityNavigator;

	private BorderPane browserContainer;

	@Override
	public void initialize() {
		super.initialize();
    doCustomBackgroundInitialization();
    browserContainer = new BorderPane();
    browserContainer.setPadding(new Insets(3));
    browserContainer.setPrefHeight(Double.MAX_VALUE);
    browserContainer.setPrefWidth(Double.MAX_VALUE);
	}

	@Override
	protected void started() {
		super.started();
	}

  protected String getName(String key){
    ResourceBundle bundle = getResources();
    return bundle.getString(key);
  }

	protected abstract void doCustomBackgroundInitialization();

}