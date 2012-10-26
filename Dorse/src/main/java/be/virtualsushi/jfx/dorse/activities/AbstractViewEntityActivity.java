package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.model.Listable;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class AbstractViewEntityActivity<N extends Node, E extends BaseEntity> extends AbstractManageEntityActivity<N, E> {


  protected ValidationErrorPanel validationPanel;

  @FXML
  protected VBox mainContainer;

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

  protected ValidationErrorPanel getValidationPanel() {
    if (validationPanel == null) {
      validationPanel = new ValidationErrorPanel();
      mainContainer.getChildren().add(validationPanel);
    }
    return validationPanel;
  }

  protected void clearValidation() {
    ValidationErrorPanel validationPanel = getValidationPanel();
    if (validationPanel != null) {
      validationPanel.clearMessages();
      validationPanel.setVisible(false);
    }
  }
}
