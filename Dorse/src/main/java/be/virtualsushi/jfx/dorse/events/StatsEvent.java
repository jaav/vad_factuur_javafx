package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.model.StatsDTO;

public class StatsEvent{

  private final StatsDTO entity;

	public StatsEvent(StatsDTO entity) {
		this.entity = entity;
	}

 	public StatsDTO getEntity() {
 		return entity;
 	}

}
