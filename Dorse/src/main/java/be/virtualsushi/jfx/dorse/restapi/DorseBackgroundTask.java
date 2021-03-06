package be.virtualsushi.jfx.dorse.restapi;

import be.virtualsushi.jfx.dorse.model.BaseEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import be.virtualsushi.jfx.dorse.activities.TaskCreator;
import be.virtualsushi.jfx.dorse.events.authentication.AuthorizationRequiredEvent;

import com.google.common.eventbus.EventBus;
import com.zenjava.jfxflow.worker.BackgroundTask;

public abstract class DorseBackgroundTask<E> extends BackgroundTask<E> {

	private EventBus eventBus;
	private final Object[] parameters;

	private final TaskCreator<DorseBackgroundTask<E>> creator;

	public DorseBackgroundTask(TaskCreator<DorseBackgroundTask<E>> taskCreator, Object... parameters) {
		super();
		this.parameters = parameters;
		this.creator = taskCreator;
		stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (State.RUNNING.equals(newValue)) {
					onPreExecute();
				}
			}
		});
	}

	protected void onPreExecute() {

	}

	@Override
	protected void onError(Throwable exception) {
		if (HttpClientErrorException.class.isInstance(exception)) {
			if (HttpStatus.FORBIDDEN == ((HttpClientErrorException) exception).getStatusCode()) {
				eventBus.post(new AuthorizationRequiredEvent());
				return;
			} else {
				System.out.println("client");
			}
		}
		if (HttpServerErrorException.class.isInstance(exception)) {
			System.out.println("server");
		}
		exception.printStackTrace();
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public Object[] getParameters() {
		return parameters;
	}



  @SuppressWarnings("unchecked")
 	protected Integer getInteger(int place) {
 		try {
      if(getParameters()[place]!=null)
 			  return (Integer) getParameters()[place];
      else return null;
 		} catch (Exception e) {
 			e.printStackTrace();
      return null;
 		}
 	}

  @SuppressWarnings("unchecked")
  protected String getString(int place) {
 		try {
      if(getParameters()[place]!=null)
 			  return (String) getParameters()[place];
      else return null;
 		} catch (Exception e) {
 			e.printStackTrace();
      return null;
 		}
 	}

  @SuppressWarnings("unchecked")
  protected boolean getBoolean(int place, boolean defaultBool) {
 		try {
      if(getParameters()[place]!=null)
 			  return (Boolean) getParameters()[place];
      else return defaultBool;
 		} catch (Exception e) {
 			e.printStackTrace();
      return defaultBool;
 		}
 	}

  @SuppressWarnings("unchecked")
  protected BaseEntity getBaseEntity(int place) {
 		try {
      if(getParameters()[place]!=null)
 			  return (BaseEntity) getParameters()[place];
      else return null;
 		} catch (Exception e) {
 			e.printStackTrace();
      return null;
 		}
 	}

 /* private Object getParameter(int place, Class<E> clazz){
    return getParameters()[place]!=null ? getParameters()[place] : null;
  }*/

	public TaskCreator<DorseBackgroundTask<E>> getCreator() {
		return creator;
	}

}
