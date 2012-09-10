package be.virtualsushi.jfx.dorse.activities;

import com.zenjava.jfxflow.worker.BackgroundTask;

public interface TaskCreator<T extends BackgroundTask<?>> {

	public T createTask();

}
