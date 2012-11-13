package be.virtualsushi.jfx.dorse.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 11/11/12
 * Time: 10:48
 */
public class SupplierResponse extends ServerResponse{
  @JsonProperty("data")
  private List<Supplier> data;

  public List<Supplier> getData() {
    return data;
  }

  public void setData(List<Supplier> suppliers) {
    this.data = suppliers;
  }
}
