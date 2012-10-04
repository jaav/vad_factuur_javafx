package be.virtualsushi.jfx.dorse.utils;

import java.util.List;

import be.virtualsushi.jfx.dorse.model.BaseEntity;

public class EntityCollectionUtils {

	public static <E extends BaseEntity> E findById(List<E> collection, Long id) {
		for (E entity : collection) {
			if (entity.getId().equals(id)) {
				return entity;
			}
		}
		throw new IllegalStateException("Entity with id:" + id + " not found");
	}

}
