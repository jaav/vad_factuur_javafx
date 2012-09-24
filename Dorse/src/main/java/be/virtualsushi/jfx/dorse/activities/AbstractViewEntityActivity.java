package be.virtualsushi.jfx.dorse.activities;

import javafx.scene.Node;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.model.Listable;

public abstract class AbstractViewEntityActivity<N extends Node, E extends BaseEntity> extends AbstractManageEntityActivity<N, E> {

	@Override
	protected void mapFields(E entity) {
		if (entity instanceof Listable) {
			title.setText(((Listable) entity).getPrintName());
		}
	}

}
