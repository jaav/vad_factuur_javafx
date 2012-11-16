package be.virtualsushi.jfx.dorse.model;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 15/11/12
 * Time: 11:16
 */
public class Status implements Listable{
  private int id;
  private String name;
  public static final String PREPARED = "PREPARED";
  public static final String SENT = "SENT";
  public static final String INVOICED = "INVOICED";
  public static final String REMINDED = "REMINDED";
  public static final String PAID = "PAID";

  public Status(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getPrintName() {
    return getName();
  }

  @Override
 	public String toString() {
 		return getName();
 	}

  public static Status getEmptyStatus(){
    return new Status(-1, "");
  }
}
