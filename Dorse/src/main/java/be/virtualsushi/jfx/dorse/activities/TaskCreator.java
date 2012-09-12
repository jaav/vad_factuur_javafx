package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

public interface TaskCreator<T extends DorseBackgroundTask<?>> {

	public T createTask();

}
