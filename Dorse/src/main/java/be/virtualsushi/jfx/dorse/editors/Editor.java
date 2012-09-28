package be.virtualsushi.jfx.dorse.editors;

import be.virtualsushi.jfx.dorse.model.BaseEntity;

public interface Editor<E extends BaseEntity> {

	void setValue(E value);

	E getValue();

}
