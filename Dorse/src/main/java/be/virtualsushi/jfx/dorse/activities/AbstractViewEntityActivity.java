package be.virtualsushi.jfx.dorse.activities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.model.Listable;

public abstract class AbstractViewEntityActivity<N extends Node, E extends BaseEntity> extends AbstractManageEntityActivity<N, E> {
	
	@FXML
	public void handleGoBack(ActionEvent event) {
		getActivityNavigator().goBack();
	}

	@Override
	protected void mapFields(E entity) {
		if (entity instanceof Listable) {
			title.setText(((Listable) entity).getPrintName());
		}
	}

}
