package be.virtualsushi.jfx.dorse.control;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.virtualsushi.jfx.dorse.activities.AbstractListActivity;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.resources.Images;

public class SortButton extends ImageButton {
  private static final Logger log = LoggerFactory.getLogger(SortButton.class);

  public SortButton(String text, String id, ActivityNavigator activityNavigator) {
    super(text);
    this.setId(id);
    this.initSorting(activityNavigator);
  }

  @Override
	protected Image getImage() {
		return Images.IMG_ADD;
	}

  public void initSorting(final ActivityNavigator activityNavigator){
    this.getStyleClass().add("borderless");
    this.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        log.debug("EVENT : " + event.toString());
        String orderOn = ((Button) event.getSource()).getId();
        AbstractListActivity.ORDER_ON = orderOn;
        activityNavigator.goTo(AppActivitiesNames.LIST_ARTICLES);
      }
    });
  }



}
