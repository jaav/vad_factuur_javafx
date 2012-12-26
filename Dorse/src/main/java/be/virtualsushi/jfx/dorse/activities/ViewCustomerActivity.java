package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.control.*;
import be.virtualsushi.jfx.dorse.dialogs.NewPersonDialog;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveAddressEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SavePersonEvent;
import be.virtualsushi.jfx.dorse.events.report.ShowReportEvent;
import be.virtualsushi.jfx.dorse.model.Person;
import be.virtualsushi.jfx.dorse.report.ReportService;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;
import be.virtualsushi.jfx.dorse.utils.Utils;
import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import name.antonsmirnov.javafx.dialog.Dialog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.dialogs.NewAddressDialog;
import be.virtualsushi.jfx.dorse.model.Address;
import be.virtualsushi.jfx.dorse.model.Customer;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ViewCustomerActivity extends AbstractViewEntityActivity<VBox, Customer> {
  private static final Logger log = LoggerFactory.getLogger(ViewInvoiceActivity.class);

  List<Person> persons;
  List<Address> addresses;
  Person currentPerson;
  Address currentAddress;


  private class SaveAddressTaskCreator implements TaskCreator<DorseBackgroundTask<List<Address>>> {

    private final Address address;

    public SaveAddressTaskCreator(Address address) {
      this.address = address;
    }

    @Override
    public DorseBackgroundTask<List<Address>> createTask() {
      return new DorseBackgroundTask<List<Address>>(this, address) {

        @Override
        protected void onPreExecute() {
          showLoadingMask();
        }

        @Override
        @SuppressWarnings("unchecked")
        protected List<Address> call() throws Exception {
          getRestApiAccessor().save((Address) getParameters()[0]);
          return (List<Address>)getRestApiAccessor().getResponse(Address.class, null, null, null, "id", "customer=" + getEntity().getId(), true, false, true).getData();
        }

        @Override
        protected void onSuccess(List<Address> value) {
          fillAddressTabs(value, getEntity().getName());
          hideLoadingMask();
        } 

        @Override
        protected void onError(Throwable exception) {
          exception.printStackTrace();
          ValidationErrorPanel validationPanel = getValidationPanel();
          if (validationPanel != null) {
            validationPanel.clearMessages();
            validationPanel.addMessage(getResources().getString("save_error") + "\n\n" + exception.getMessage());
            validationPanel.setVisible(true);
          }
          hideLoadingMask();
        }

      };
    }

  }  

  private class DeleteAddressTaskCreator implements TaskCreator<DorseBackgroundTask<List<Address>>> {

    private final Address address;

    public DeleteAddressTaskCreator(Address address) {
      this.address = address;
    }

    @Override
    public DorseBackgroundTask<List<Address>> createTask() {
      return new DorseBackgroundTask<List<Address>>(this, address) {

        @Override
        protected void onPreExecute() {
        }

        @Override
        @SuppressWarnings("unchecked")
        protected List<Address> call() throws Exception {
          getRestApiAccessor().delete((Address) getParameters()[0]);
          return (List<Address>)getRestApiAccessor().getResponse(Address.class, null, null, null, "id", "customer=" + getEntity().getId(), true, false, true).getData();
        }

        @Override
        protected void onSuccess(List<Address> value) {
          fillAddressTabs(value, getEntity().getName());
          hideLoadingMask();
        }

        @Override
        protected void onError(Throwable exception) {
          exception.printStackTrace();
          ValidationErrorPanel validationPanel = getValidationPanel();
          if (validationPanel != null) {
            validationPanel.clearMessages();
            validationPanel.addMessage(getResources().getString("delete_error") + "\n\n" + exception.getMessage());
            validationPanel.setVisible(true);
          }
          hideLoadingMask();
        }
      };
    }

  }


  private class SavePersonTaskCreator implements TaskCreator<DorseBackgroundTask<List<Person>>> {

    private final Person person;

    public SavePersonTaskCreator(Person person) {
      this.person = person;
    }

    @Override
    public DorseBackgroundTask<List<Person>> createTask() {
      return new DorseBackgroundTask<List<Person>>(this, person) {

        @Override
        protected void onPreExecute() {
          showLoadingMask();
        }

        @Override
        @SuppressWarnings("unchecked")
        protected List<Person> call() throws Exception {
          getRestApiAccessor().save((Person) getParameters()[0]);
          return (List<Person>)getRestApiAccessor().getResponse(Person.class, null, null, null, "id", "customer=" + getEntity().getId(), true, false, true).getData();
        }

        @Override
        protected void onSuccess(List<Person> value) {
          showPersonList(value);
          hideLoadingMask();
        }

        @Override
        protected void onError(Throwable exception) {
          exception.printStackTrace();
          ValidationErrorPanel validationPanel = getValidationPanel();
          if (validationPanel != null) {
            validationPanel.clearMessages();
            validationPanel.addMessage(getResources().getString("save_error") + "\n\n" + exception.getMessage());
            validationPanel.setVisible(true);
          }
          hideLoadingMask();
        }
      };
    }

  }

  private class DeletePersonTaskCreator implements TaskCreator<DorseBackgroundTask<List<Person>>> {

      private final Person person;

      public DeletePersonTaskCreator(Person person) {
        this.person = person;
      }

      @Override
      public DorseBackgroundTask<List<Person>> createTask() {
        return new DorseBackgroundTask<List<Person>>(this, person) {

          @Override
          protected void onPreExecute() {
          }

          @Override
          @SuppressWarnings("unchecked")
          protected List<Person> call() throws Exception {
            getRestApiAccessor().delete((Person) getParameters()[0]);
            return (List<Person>)getRestApiAccessor().getResponse(Person.class, null, null, null, "id", "customer=" + getEntity().getId(), true, false, true).getData();
          }

          @Override
          protected void onSuccess(List<Person> value) {
            showPersonList(value);
            setCurrentPerson(null);
            hideLoadingMask();
          }

          @Override
          protected void onError(Throwable exception) {
            exception.printStackTrace();
            ValidationErrorPanel validationPanel = getValidationPanel();
            if (validationPanel != null) {
              validationPanel.clearMessages();
              validationPanel.addMessage(getResources().getString("delete_error") + "\n\n" + exception.getMessage());
              validationPanel.setVisible(true);
            }
            hideLoadingMask();
          }
        };
      }

    }

  private class GenerateLabelTaskCreator implements TaskCreator<DorseBackgroundTask<String>> {

 		private final Customer customer;
    private final Address address;
    private final Person person;

 		public GenerateLabelTaskCreator(Customer customer, Address address) {
       this.customer = customer;
       this.address = address;
       if(customer.getPerson()!=null && !customer.getPerson().isEmpty())
         this.person = customer.getPerson().get(0);
       else
         this.person = null;
 		}

 		@Override
 		public DorseBackgroundTask<String> createTask() {
 			return new DorseBackgroundTask<String>(this, customer, address, person) {

 				@Override
 				protected void onPreExecute() {
 					showLoadingMask();
 				}

 				@SuppressWarnings("unchecked")
 				@Override
 				protected String call() throws Exception {
           if(getParameters()[2]!=null && StringUtils.isNotBlank(((Person) getParameters()[2]).getName()))
 					   return reportService.createAddressLabel((Customer) getParameters()[0], (Address) getParameters()[1], (Person) getParameters()[2]);
           else
             return reportService.createAddressLabel((Customer) getParameters()[0], (Address) getParameters()[1]);
 				}

 				@Override
 				protected void onSuccess(String value) {
 					hideLoadingMask();
 					getEventBus().post(new ShowReportEvent(value));
 				}

         @Override
         protected void onError(Throwable exception) {
           log.warn(exception.getMessage());
           hideLoadingMask();
           super.onError(exception);    //To change body of overridden methods use File | Settings | File Templates.
         }
       };
 		}

 	}

  @Autowired
 	private ReportService reportService;

  @FXML
  protected Label idField, vatField, ibanField, remarkField;

  @FXML
  protected TabPane addressesList;

  @FXML
  protected ListView personsList;

  @Override
  protected void mapFields(Customer viewingEntity) {
    super.mapFields(viewingEntity);
    title.setText(viewingEntity.getName());
    idField.setText(String.valueOf(viewingEntity.getId()));
    vatField.setText(viewingEntity.getVat());
    ibanField.setText(viewingEntity.getIban());
    remarkField.setText(viewingEntity.getRemark());
    fillAddressTabs(viewingEntity.getAddress(), viewingEntity.getName());
    showPersonList(viewingEntity.getPerson());
  }

  private void fillAddressTabs(List<Address> addresses, String title) {
    this.addresses = addresses;
    addressesList.getTabs().clear();
    int addressIndex = 1;
    for (Address address : addresses) {
      Tab tab = new Tab(StringUtils.isNotBlank(address.getAddress()) ? Utils.shorten(address.getAddress(), 16) : getResources().getString("address"));
      
      VBox container = new VBox();
      
      ViewAddressControl addressView = new ViewAddressControl();
      addressView.setTitle(title);
      addressView.setPadding(new Insets(20));
      addressView.setResources(getResources());
      addressView.setValue(address);
      
      
      EditButton editButton = new EditButton();
      editButton.setId("edit_"+address.getId());
      editButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          Button btn = (Button) event.getSource();
          try {
            Address address = findAddress(Long.parseLong(btn.getId().substring(btn.getId().indexOf("_") + 1)));
            showDialog(String.format(getResources().getString("new.customer.address.dialog.title"), getEntity().getName()), NewAddressDialog.class, getEntity().getId(), address);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
      
      DeleteButton deleteButton = new DeleteButton();
      deleteButton.setId("delete_" + address.getId());
      deleteButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          Button btn = (Button) event.getSource();
          try {
            setCurrentAddress(findAddress(Long.parseLong(btn.getId().substring(btn.getId().indexOf("_") + 1))));
          } catch (Exception e) {
            e.printStackTrace();
          }
          Dialog.buildConfirmation("Delete this address?", "Do you really want to permanently delete " + getCurrentAddress().getAddress() + " from the database?")
              .addYesButton(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                  if (getCurrentAddress() != null) {
                    deleteAddress(getCurrentAddress());
                  }
                }
              })
              .addCancelButton(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                  //To change body of implemented methods use File | Settings | File Templates.
                }
              })
              .build()
              .show();
        }
      });



      PrintButton printButton = new PrintButton();
      printButton.setId("print_"+address.getId());
      printButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          Button btn = (Button) event.getSource();
          try {
            Address address = findAddress(Long.parseLong(btn.getId().substring(btn.getId().indexOf("_") + 1)));
            Customer customer = getCurrentCustomer();
            doInBackground(new GenerateLabelTaskCreator(customer, address));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
      HBox buttonContainer = new HBox();
      buttonContainer.getChildren().add(editButton);
      buttonContainer.getChildren().add(deleteButton);
      buttonContainer.getChildren().add(printButton);
      container.getChildren().add(addressView);
      container.getChildren().add(buttonContainer);
      tab.setContent(container);
      addressesList.getTabs().add(tab);
      addressIndex++;
    }
  }

  private void showPersonList(List<Person> persons) {
    this.persons = persons;
    personsList.getItems().clear();
    int personIndex = 1;
    for (Person person : persons) {
      HBox personBox = new HBox();
      personBox.setPrefWidth(400);
      EditButton editButton = new EditButton();
      editButton.setId("edit_"+person.getId());
      editButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          Button btn = (Button) event.getSource();
          try {
            Person person = findPerson(Long.parseLong(btn.getId().substring(btn.getId().indexOf("_") + 1)));
            showDialog(String.format(getResources().getString("new.customer.person.dialog.title"), getEntity().getName()), NewPersonDialog.class, getEntity().getId(), person);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
      DeleteButton deleteButton = new DeleteButton();
      deleteButton.setId("delete_"+person.getId());
      deleteButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          Button btn = (Button)event.getSource();
          try{
            setCurrentPerson(findPerson(Long.parseLong(btn.getId().substring(btn.getId().indexOf("_") + 1))));
            if(getCurrentPerson()!=null)
              Dialog.buildConfirmation("Delete this person?", "Do you really want to permanently delete " + getCurrentPerson().getName() + " from the database?")
                .addYesButton(new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent actionEvent) {
                    if(getCurrentPerson()!=null){
                      deletePerson(getCurrentPerson());
                    }
                  }
                })
                .addCancelButton(new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent actionEvent) {
                    //To change body of implemented methods use File | Settings | File Templates.
                  }
                })
                .build()
                .show();
          }
          catch(Exception e){
            e.printStackTrace();
          }
        }
      });
      Label title = new Label(person.getTitle() + " " + person.getName()+" ("+person.getEmail()+" - "+person.getPhone()+")");
      title.setPrefWidth(300);
      personBox.getChildren().add(title);
      personBox.getChildren().add(editButton);
      personBox.getChildren().add(deleteButton);
      personBox.setMargin(editButton, new Insets(0, 0, 0, 60));
      //personsList.getItems().add(new Label(person.getTitle() + " " + person.getName()+" ("+person.getEmail()+" - "+person.getPhone()+")"));
      personsList.getItems().add(personBox);
      personIndex++;
    }
  }

  private String getStyleClasses(List<String> classes) {
    if (classes != null && !classes.isEmpty()) {
      StringBuffer sb = new StringBuffer();
      for (String aClass : classes) {
        sb.append(aClass).append(" - ");
      }
      return sb.toString();
    }
    return "";
  }

  @FXML
  protected void handleAddAddress(ActionEvent event) {
    showDialog(String.format(getResources().getString("new.customer.address.dialog.title"), getEntity().getName()), NewAddressDialog.class, getEntity().getId());
  }

  @FXML
  protected void handleAddPerson(ActionEvent event) {
    showDialog(String.format(getResources().getString("new.customer.person.dialog.title"), getEntity().getName()), NewPersonDialog.class, getEntity().getId());
  }

  @Override
  protected void doCustomBackgroundInitialization(Customer entity) {

  }

  @Override
  protected void started() {
    super.started();
    clearValidation();
    //clearDependencies();
  }

  @Subscribe
  public void onSaveAddress(SaveAddressEvent event) {
    doInBackground(new SaveAddressTaskCreator(event.getEntity()));
  }

  @Subscribe
  public void onSavePerson(SavePersonEvent event) {
    doInBackground(new SavePersonTaskCreator(event.getEntity()));
  }

  public void deletePerson(Person person) {
    doInBackground(new DeletePersonTaskCreator(person));
  }

  public void deleteAddress(Address address) {
    doInBackground(new DeleteAddressTaskCreator(address));
  }

  private void clearDependencies() {
    addressesList.getTabs().clear();
    personsList.getItems().clear();
  }

  private Person findPerson(Long personId) {
 		for (Person person : persons) {
 			if (person.getId().equals(personId)) {
 				return person;
 			}
 		}
 		throw new IllegalStateException("Can't find Person with id=" + personId);
 	}   

  private Address findAddress(Long addressId) {
 		for (Address address : addresses) {
 			if (address.getId().equals(addressId)) {
 				return address;
 			}
 		}
 		throw new IllegalStateException("Can't find Address with id=" + addressId);
 	}
  
  private Customer getCurrentCustomer(){
    return this.getEntity();
  }

  public Person getCurrentPerson() {
    return currentPerson;
  }

  public void setCurrentPerson(Person currentPerson) {
    this.currentPerson = currentPerson;
  }

  public Address getCurrentAddress() {
    return currentAddress;
  }

  public void setCurrentAddress(Address currentAddress) {
    this.currentAddress = currentAddress;
  }
}
